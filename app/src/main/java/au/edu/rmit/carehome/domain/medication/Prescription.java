package au.edu.rmit.carehome.domain.medication;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Aggregates prescription items linked to a resident and doctor for auditing and
 * scheduling calculations.
 */
public class Prescription implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID residentId;
    private final UUID doctorId;
    private final LocalDateTime createdAt;
    private final List<PrescriptionItem> items = new ArrayList<>();

    public Prescription(UUID id, UUID residentId, UUID doctorId, LocalDateTime createdAt) {
        this.id = Objects.requireNonNull(id, "id");
        this.residentId = Objects.requireNonNull(residentId, "residentId");
        this.doctorId = Objects.requireNonNull(doctorId, "doctorId");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    }

    public UUID getId() {
        return id;
    }

    public UUID getResidentId() {
        return residentId;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<PrescriptionItem> getItems() {
        return items;
    }

    public void addItem(PrescriptionItem item) {
        items.add(Objects.requireNonNull(item));
    }
}
