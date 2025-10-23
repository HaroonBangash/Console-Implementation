package au.edu.rmit.carehome.app;

import au.edu.rmit.carehome.common.PasswordHasher;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.facility.Bed;
import au.edu.rmit.carehome.domain.facility.Room;
import au.edu.rmit.carehome.domain.facility.Ward;
import au.edu.rmit.carehome.domain.medication.MedicationAdministration;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.medication.PrescriptionItem;
import au.edu.rmit.carehome.domain.resident.Gender;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.staff.Doctor;
import au.edu.rmit.carehome.domain.staff.Manager;
import au.edu.rmit.carehome.domain.staff.Nurse;
import au.edu.rmit.carehome.domain.staff.Shift;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Design decisions: Creates deterministic seed data for demos/tests when no persisted state exists.
 */
public final class SampleDataSeeder {
    private SampleDataSeeder() {
    }

    public static void seed(CareHome careHome) {
        if (!careHome.getWards().isEmpty()) {
            return;
        }
        createWards(careHome);
        List<Nurse> nurses = createStaff(careHome);
        Doctor doctor = careHome.getStaff().values().stream()
                .filter(Doctor.class::isInstance)
                .map(Doctor.class::cast)
                .findFirst()
                .orElseThrow();
        assignShifts(careHome, nurses, doctor);
        createSampleResidents(careHome, doctor, nurses);
        careHome.checkCompliance();
    }

    private static void createWards(CareHome careHome) {
        for (int wardIndex = 0; wardIndex < 2; wardIndex++) {
            Ward ward = new Ward(UUID.randomUUID(), "Ward " + (wardIndex + 1));
            careHome.getWards().add(ward);
            for (int roomIndex = 0; roomIndex < 6; roomIndex++) {
                Room room = new Room(UUID.randomUUID(), ward.getId());
                ward.addRoom(room);
                int beds = 1 + (roomIndex % 4);
                for (int bedIndex = 0; bedIndex < beds; bedIndex++) {
                    Gender gender = (bedIndex + roomIndex) % 2 == 0 ? Gender.MALE : Gender.FEMALE;
                    Bed bed = new Bed(UUID.randomUUID(), room.getId(), gender, bedIndex == 0 && roomIndex == 0);
                    room.addBed(bed);
                }
            }
        }
    }

    private static List<Nurse> createStaff(CareHome careHome) {
        Manager manager = new Manager(UUID.randomUUID(), "Alice Manager", "manager",
                PasswordHasher.hash("manager123"));
        Doctor doctor = new Doctor(UUID.randomUUID(), "Dr Bob", "doctor", PasswordHasher.hash("doctor123"));
        careHome.getStaff().put(manager.getId(), manager);
        careHome.getStaff().put(doctor.getId(), doctor);

        List<Nurse> nurses = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Nurse nurse = new Nurse(UUID.randomUUID(), "Nurse " + (char) ('A' + i),
                    "nurse" + (i + 1), PasswordHasher.hash("nurse" + (i + 1)));
            careHome.getStaff().put(nurse.getId(), nurse);
            nurses.add(nurse);
        }
        return nurses;
    }

    private static void assignShifts(CareHome careHome, List<Nurse> nurses, Doctor doctor) {
        DayOfWeek[] days = DayOfWeek.values();
        for (int i = 0; i < days.length; i++) {
            Nurse dayNurse = nurses.get(i % nurses.size());
            Nurse eveningNurse = nurses.get((i + 1) % nurses.size());
            careHome.getShifts().add(new Shift(dayNurse.getId(), days[i], LocalTime.of(8, 0), LocalTime.of(16, 0)));
            careHome.getShifts().add(new Shift(eveningNurse.getId(), days[i], LocalTime.of(14, 0), LocalTime.of(22, 0)));
            careHome.getShifts().add(new Shift(doctor.getId(), days[i], LocalTime.of(9, 0), LocalTime.of(11, 0)));
        }
    }

    private static void createSampleResidents(CareHome careHome, Doctor doctor, List<Nurse> nurses) {
        List<Bed> beds = careHome.getAllBeds();
        if (beds.isEmpty()) {
            return;
        }
        Resident john = new Resident(UUID.randomUUID(), "John Elder", Gender.MALE,
                LocalDate.of(1942, 3, 4), Set.of("Diabetes"));
        assignResidentToFirstMatchingBed(careHome, beds, john, Gender.MALE);
        addSamplePrescription(careHome, doctor, john, "Metformin", 500, "mg");

        Resident mary = new Resident(UUID.randomUUID(), "Mary Care", Gender.FEMALE,
                LocalDate.of(1938, 7, 19), Set.of("Hypertension"));
        assignResidentToFirstMatchingBed(careHome, beds, mary, Gender.FEMALE);
        addSamplePrescription(careHome, doctor, mary, "Lisinopril", 10, "mg");

        // Record one administration to demonstrate audit trail
        if (!careHome.getAdministrations().isEmpty()) {
            return;
        }
        careHome.getAdministrations().add(new MedicationAdministration(UUID.randomUUID(), john.getId(),
                careHome.getPrescriptions().get(0).getItems().get(0).getId(), LocalTime.of(8, 0),
                LocalDateTime.now().minusDays(1), nurses.get(0).getId(), "Initial dose"));
    }

    private static void assignResidentToFirstMatchingBed(CareHome careHome, List<Bed> beds, Resident resident, Gender gender) {
        for (Bed bed : beds) {
            if (bed.getOccupantResidentId().isEmpty() && bed.getGenderTag() == gender) {
                bed.assignResident(resident.getId());
                resident.setBedId(bed.getId());
                resident.addHistoryEntry("Seed admission to bed " + bed.getId());
                careHome.getResidents().put(resident.getId(), resident);
                return;
            }
        }
    }

    private static void addSamplePrescription(CareHome careHome, Doctor doctor, Resident resident,
                                              String drug, double dose, String unit) {
        Prescription prescription = new Prescription(UUID.randomUUID(), resident.getId(), doctor.getId(), LocalDateTime.now());
        prescription.addItem(new PrescriptionItem(UUID.randomUUID(), drug, dose, unit, List.of(LocalTime.of(8, 0), LocalTime.of(20, 0))));
        careHome.getPrescriptions().add(prescription);
    }
}
