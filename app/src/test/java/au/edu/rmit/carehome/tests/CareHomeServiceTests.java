package au.edu.rmit.carehome.tests;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.common.ComplianceException;
import au.edu.rmit.carehome.common.ValidationException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.facility.Bed;
import au.edu.rmit.carehome.domain.facility.Room;
import au.edu.rmit.carehome.domain.facility.Ward;
import au.edu.rmit.carehome.domain.medication.PrescriptionItem;
import au.edu.rmit.carehome.domain.resident.Gender;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.staff.Doctor;
import au.edu.rmit.carehome.domain.staff.Manager;
import au.edu.rmit.carehome.domain.staff.Nurse;
import au.edu.rmit.carehome.domain.staff.Shift;
import au.edu.rmit.carehome.persistence.Archiver;
import au.edu.rmit.carehome.persistence.FileSerializer;
import au.edu.rmit.carehome.services.AdmissionService;
import au.edu.rmit.carehome.services.AuditService;
import au.edu.rmit.carehome.services.MedicationService;
import au.edu.rmit.carehome.services.RosterService;
import au.edu.rmit.carehome.services.MedicationService.PrescriptionItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Design decisions: Integration-style tests cover key business rules without spinning up the UI.
 */
public class CareHomeServiceTests {
    private CareHome careHome;
    private Clock clock;
    private AuditService auditService;
    private AdmissionService admissionService;
    private RosterService rosterService;
    private MedicationService medicationService;
    private Manager manager;
    private Doctor doctor;
    private Nurse nurse;

    @BeforeEach
    void setUp() {
        careHome = new CareHome();
        clock = Clock.fixed(Instant.parse("2025-01-06T09:00:00Z"), ZoneOffset.UTC); // Monday 09:00 UTC
        auditService = new AuditService(careHome, clock);
        rosterService = new RosterService(careHome);
        Archiver archiver = (c, r) -> { };
        admissionService = new AdmissionService(careHome, auditService, clock, archiver);
        medicationService = new MedicationService(careHome, auditService, rosterService, clock);
        setupFacility();
        setupStaff();
    }

    @Test
    void admitResidentToVacantBedSucceeds() {
        Resident resident = createResident(Gender.MALE, "John Test");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident, bed.getId());
        assertEquals(resident.getId(), bed.getOccupantResidentId().orElseThrow());
        assertTrue(careHome.getResidents().containsKey(resident.getId()));
    }

    @Test
    void admitResidentToOccupiedBedFails() {
        Resident resident1 = createResident(Gender.MALE, "Resident One");
        Resident resident2 = createResident(Gender.MALE, "Resident Two");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident1, bed.getId());
        assertThrows(ValidationException.class,
                () -> admissionService.admitResident(manager, resident2, bed.getId()));
    }

    @Test
    void doctorOnlyPrescriptionEnforced() {
        Resident resident = createResident(Gender.MALE, "Patient");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident, bed.getId());
        assertThrows(AuthorizationException.class, () -> medicationService.createPrescription(nurse,
                resident.getId(), List.of(new PrescriptionItemRequest("Drug", 1, "mg",
                        List.of(LocalTime.of(9, 0))))));
    }

    @Test
    void nurseOnlyAdministrationEnforced() {
        Resident resident = createResident(Gender.MALE, "Patient");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident, bed.getId());
        medicationService.createPrescription(doctor, resident.getId(),
                List.of(new PrescriptionItemRequest("Drug", 1, "mg", List.of(LocalTime.of(9, 0)))));
        PrescriptionItem item = careHome.getPrescriptions().get(0).getItems().get(0);
        assertThrows(AuthorizationException.class, () -> medicationService.recordAdministration(doctor,
                resident.getId(), item.getId(), LocalTime.of(9, 0), ""));
    }

    @Test
    void offShiftActionRejected() {
        // 23:00 UTC Monday (outside shifts)
        Clock offShiftClock = Clock.fixed(Instant.parse("2025-01-06T23:00:00Z"), ZoneOffset.UTC);
        MedicationService offShiftMedicationService = new MedicationService(careHome, auditService, rosterService, offShiftClock);
        Resident resident = createResident(Gender.MALE, "Night Patient");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident, bed.getId());
        assertThrows(AuthorizationException.class, () -> offShiftMedicationService.createPrescription(doctor,
                resident.getId(), List.of(new PrescriptionItemRequest("Drug", 1, "mg",
                        List.of(LocalTime.of(9, 0))))));
    }

    @Test
    void complianceViolationDetected() {
        careHome.getShifts().clear();
        careHome.getShifts().add(new Shift(nurse.getId(), DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0)));
        assertThrows(ComplianceException.class, careHome::checkCompliance);
    }

    @Test
    void serializationRoundTripRestoresState() throws Exception {
        Resident resident = createResident(Gender.MALE, "Serializable Resident");
        Bed bed = careHome.getAllBeds().get(0);
        admissionService.admitResident(manager, resident, bed.getId());
        Path file = Files.createTempFile("carehome", ".ser");
        try {
            new FileSerializer().save(careHome, file);
            CareHome restored = new FileSerializer().load(file);
            assertEquals(careHome.getResidents().size(), restored.getResidents().size());
            assertEquals(careHome.getAllBeds().size(), restored.getAllBeds().size());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    private Resident createResident(Gender gender, String name) {
        return new Resident(UUID.randomUUID(), name, gender, LocalDate.of(1940, 1, 1), Set.of("Test"));
    }

    private void setupFacility() {
        Ward ward = new Ward(UUID.randomUUID(), "Test Ward");
        Room room = new Room(UUID.randomUUID(), ward.getId());
        Bed bed = new Bed(UUID.randomUUID(), room.getId(), Gender.MALE, false);
        room.addBed(bed);
        ward.addRoom(room);
        careHome.getWards().add(ward);
    }

    private void setupStaff() {
        manager = new Manager(UUID.randomUUID(), "Manager", "manager", "hash");
        doctor = new Doctor(UUID.randomUUID(), "Doctor", "doctor", "hash");
        nurse = new Nurse(UUID.randomUUID(), "Nurse", "nurse", "hash");
        careHome.getStaff().put(manager.getId(), manager);
        careHome.getStaff().put(doctor.getId(), doctor);
        careHome.getStaff().put(nurse.getId(), nurse);
        careHome.getShifts().add(new Shift(nurse.getId(), DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(16, 0)));
        careHome.getShifts().add(new Shift(doctor.getId(), DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(11, 0)));
    }
}
