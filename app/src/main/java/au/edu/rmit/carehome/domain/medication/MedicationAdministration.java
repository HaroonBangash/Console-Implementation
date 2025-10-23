package au.edu.rmit.carehome.domain.medication;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Tracks scheduled vs actual administration for compliance and auditing.
 */
public class MedicationAdministration implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID residentId;
    private final UUID prescriptionItemId;
    private final LocalTime scheduledTime;
    private LocalDateTime administeredTime;
    private final UUID nurseId;
    private final String notes;

    public MedicationAdministration(UUID id, UUID residentId, UUID prescriptionItemId, LocalTime scheduledTime,
                                    LocalDateTime administeredTime, UUID nurseId, String notes) {
        this.id = Objects.requireNonNull(id, "id");
        this.residentId = Objects.requireNonNull(residentId, "residentId");
        this.prescriptionItemId = Objects.requireNonNull(prescriptionItemId, "prescriptionItemId");
        this.scheduledTime = Objects.requireNonNull(scheduledTime, "scheduledTime");
        this.administeredTime = administeredTime;
        this.nurseId = Objects.requireNonNull(nurseId, "nurseId");
        this.notes = notes;
    }

    public UUID getId() {
        return id;
    }

    public UUID getResidentId() {
        return residentId;
    }

    public UUID getPrescriptionItemId() {
        return prescriptionItemId;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public LocalDateTime getAdministeredTime() {
        return administeredTime;
    }

    public void setAdministeredTime(LocalDateTime administeredTime) {
        this.administeredTime = administeredTime;
    }

    public UUID getNurseId() {
        return nurseId;
    }

    public String getNotes() {
        return notes;
    }
}
