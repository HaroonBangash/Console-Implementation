package au.edu.rmit.carehome.domain.facility;

import au.edu.rmit.carehome.domain.resident.Gender;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Design decisions: Bed stores occupancy as residentId to avoid circular serialization; genderTag
 * communicates suitability in UI and admission validation.
 */
public class Bed implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID roomId;
    private final Gender genderTag;
    private final boolean isolation;
    private UUID occupantResidentId;

    public Bed(UUID id, UUID roomId, Gender genderTag, boolean isolation) {
        this.id = Objects.requireNonNull(id, "id");
        this.roomId = Objects.requireNonNull(roomId, "roomId");
        this.genderTag = Objects.requireNonNull(genderTag, "genderTag");
        this.isolation = isolation;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRoomId() {
        return roomId;
    }

    public Gender getGenderTag() {
        return genderTag;
    }

    public boolean isIsolation() {
        return isolation;
    }

    public Optional<UUID> getOccupantResidentId() {
        return Optional.ofNullable(occupantResidentId);
    }

    public void assignResident(UUID residentId) {
        this.occupantResidentId = residentId;
    }

    public void vacate() {
        this.occupantResidentId = null;
    }
}
