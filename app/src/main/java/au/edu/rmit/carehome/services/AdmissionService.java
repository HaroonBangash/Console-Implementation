package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.common.NotFoundException;
import au.edu.rmit.carehome.common.ValidationException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.facility.Bed;
import au.edu.rmit.carehome.domain.medication.AuditActionType;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.resident.Gender;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Staff;
import au.edu.rmit.carehome.persistence.Archiver;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;

/**
 * Design decisions: Manages the resident lifecycle with gender/isolation rules and audit logging,
 * delegating persistence concerns to the injected archiver.
 */
public class AdmissionService {
    private final CareHome careHome;
    private final AuditService auditService;
    private final Clock clock;
    private final Archiver archiver;

    public AdmissionService(CareHome careHome, AuditService auditService, Clock clock, Archiver archiver) {
        this.careHome = careHome;
        this.auditService = auditService;
        this.clock = clock;
        this.archiver = archiver;
    }

    public void admitResident(Staff actor, Resident resident, UUID bedId) {
        ensureManager(actor);
        Bed bed = findBed(bedId);
        ensureVacant(bed);
        ensureGenderMatch(resident.getGender(), bed);
        bed.assignResident(resident.getId());
        resident.setBedId(bed.getId());
        resident.addHistoryEntry("Admitted to bed " + bed.getId() + " at " + LocalDateTime.now(clock));
        careHome.getResidents().put(resident.getId(), resident);
        auditService.record(actor, AuditActionType.ADMIT, resident.getId().toString(), "Admitted to bed " + bedId);
    }

    public void moveResident(Staff actor, UUID residentId, UUID targetBedId) {
        ensureManager(actor);
        Resident resident = findResident(residentId);
        Bed currentBed = resident.getBedId() != null ? findBed(resident.getBedId()) : null;
        Bed target = findBed(targetBedId);
        ensureVacant(target);
        ensureGenderMatch(resident.getGender(), target);
        if (currentBed != null) {
            currentBed.vacate();
        }
        target.assignResident(resident.getId());
        resident.setBedId(target.getId());
        resident.addHistoryEntry("Moved to bed " + target.getId() + " at " + LocalDateTime.now(clock));
        auditService.record(actor, AuditActionType.MOVE, residentId.toString(), "Moved to bed " + targetBedId);
    }

    public void dischargeResident(Staff actor, UUID residentId) {
        ensureManager(actor);
        Resident resident = findResident(residentId);
        Bed bed = resident.getBedId() != null ? findBed(resident.getBedId()) : null;
        if (bed != null) {
            bed.vacate();
        }
        archiver.archiveResident(careHome, resident);
        removeResidentArtifacts(resident);
        careHome.getResidents().remove(residentId);
        auditService.record(actor, AuditActionType.DISCHARGE, residentId.toString(), "Discharged resident");
    }

    private void removeResidentArtifacts(Resident resident) {
        UUID residentId = resident.getId();
        Iterator<Prescription> prescriptionIterator = careHome.getPrescriptions().iterator();
        while (prescriptionIterator.hasNext()) {
            Prescription prescription = prescriptionIterator.next();
            if (prescription.getResidentId().equals(residentId)) {
                prescriptionIterator.remove();
            }
        }
        careHome.getAdministrations().removeIf(administration -> administration.getResidentId().equals(residentId));
    }

    private void ensureManager(Staff actor) {
        if (actor == null || actor.getRole() != Role.MANAGER) {
            throw new AuthorizationException("Only managers can manage beds");
        }
    }

    private Bed findBed(UUID bedId) {
        return careHome.findBed(bedId).orElseThrow(() -> new NotFoundException("Bed not found"));
    }

    private Resident findResident(UUID residentId) {
        Resident resident = careHome.getResidents().get(residentId);
        if (resident == null) {
            throw new NotFoundException("Resident not found");
        }
        return resident;
    }

    private void ensureVacant(Bed bed) {
        if (bed.getOccupantResidentId().isPresent()) {
            throw new ValidationException("Bed is already occupied");
        }
    }

    private void ensureGenderMatch(Gender gender, Bed bed) {
        if (bed.getGenderTag() != gender) {
            throw new ValidationException("Gender incompatible with bed assignment");
        }
    }
}
