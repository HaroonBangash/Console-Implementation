package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.common.NotFoundException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.medication.AuditActionType;
import au.edu.rmit.carehome.domain.medication.MedicationAdministration;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.medication.PrescriptionItem;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Design decisions: Keeps medication workflows cohesive by orchestrating prescriptions and
 * administrations with roster-aware authorisation.
 */
public class MedicationService {
    private final CareHome careHome;
    private final AuditService auditService;
    private final RosterService rosterService;
    private final Clock clock;

    public MedicationService(CareHome careHome, AuditService auditService, RosterService rosterService, Clock clock) {
        this.careHome = careHome;
        this.auditService = auditService;
        this.rosterService = rosterService;
        this.clock = clock;
    }

    public Prescription createPrescription(Staff actor, UUID residentId, List<PrescriptionItemRequest> items) {
        ensureDoctor(actor);
        ensureOnShift(actor);
        Resident resident = findResident(residentId);
        Prescription prescription = new Prescription(UUID.randomUUID(), residentId, actor.getId(), LocalDateTime.now(clock));
        for (PrescriptionItemRequest item : items) {
            prescription.addItem(new PrescriptionItem(UUID.randomUUID(), item.drugName(), item.dose(),
                    item.unit(), item.scheduleTimes()));
        }
        careHome.getPrescriptions().add(prescription);
        resident.addHistoryEntry("Prescription added " + prescription.getId() + " at " + LocalDateTime.now(clock));
        auditService.record(actor, AuditActionType.PRESCRIBE, prescription.getId().toString(),
                "Prescription for resident " + residentId);
        return prescription;
    }

    public MedicationAdministration recordAdministration(Staff actor, UUID residentId, UUID prescriptionItemId,
                                                         LocalTime scheduledTime, String notes) {
        ensureNurse(actor);
        ensureOnShift(actor);
        Resident resident = findResident(residentId);
        PrescriptionItem item = findPrescriptionItem(prescriptionItemId);
        MedicationAdministration administration = new MedicationAdministration(UUID.randomUUID(), residentId,
                item.getId(), scheduledTime, LocalDateTime.now(clock), actor.getId(), notes);
        careHome.getAdministrations().add(administration);
        resident.addHistoryEntry("Medication administered " + item.getDrugName() + " at " + LocalDateTime.now(clock));
        auditService.record(actor, AuditActionType.ADMINISTER, administration.getId().toString(),
                "Administered " + item.getDrugName());
        return administration;
    }

    public List<DueDose> getDueDoses(LocalDate date) {
        List<DueDose> due = new ArrayList<>();
        for (Prescription prescription : careHome.getPrescriptions()) {
            Resident resident = careHome.getResidents().get(prescription.getResidentId());
            if (resident == null) {
                continue;
            }
            for (PrescriptionItem item : prescription.getItems()) {
                for (LocalTime time : item.getScheduleTimes()) {
                    boolean administered = careHome.getAdministrations().stream()
                            .anyMatch(administration -> administration.getPrescriptionItemId().equals(item.getId())
                                    && administration.getScheduledTime().equals(time)
                                    && administration.getAdministeredTime().toLocalDate().equals(date));
                    if (!administered) {
                        due.add(new DueDose(resident, item, time));
                    }
                }
            }
        }
        return due;
    }

    private void ensureDoctor(Staff actor) {
        if (actor == null || actor.getRole() != Role.DOCTOR) {
            throw new AuthorizationException("Only doctors can prescribe");
        }
    }

    private void ensureNurse(Staff actor) {
        if (actor == null || actor.getRole() != Role.NURSE) {
            throw new AuthorizationException("Only nurses can administer medication");
        }
    }

    private void ensureOnShift(Staff staff) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (!rosterService.isOnShift(staff.getId(), now)) {
            auditService.record(staff, AuditActionType.LOGIN_FAILURE, staff.getId().toString(),
                    "Attempted action off shift");
            throw new AuthorizationException("Staff member is not on shift");
        }
    }

    private Resident findResident(UUID residentId) {
        Resident resident = careHome.getResidents().get(residentId);
        if (resident == null) {
            throw new NotFoundException("Resident not found");
        }
        return resident;
    }

    private PrescriptionItem findPrescriptionItem(UUID prescriptionItemId) {
        return careHome.getPrescriptions().stream()
                .flatMap(prescription -> prescription.getItems().stream())
                .filter(item -> item.getId().equals(prescriptionItemId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Prescription item not found"));
    }

    /**
     * Design decisions: DTO powering due-dose UI sections without leaking domain internals.
     */
    public record DueDose(Resident resident, PrescriptionItem item, LocalTime scheduledTime) {
    }

    /**
     * Design decisions: Builder-friendly request for prescription item creation.
     */
    public record PrescriptionItemRequest(String drugName, double dose, String unit, List<LocalTime> scheduleTimes) {
    }
}
