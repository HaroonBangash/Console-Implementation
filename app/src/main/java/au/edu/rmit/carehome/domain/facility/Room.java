package au.edu.rmit.carehome.domain.facility;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Room aggregates its beds for UI grouping and future infection control logic.
 */
public class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID wardId;
    private final List<Bed> beds = new ArrayList<>();

    public Room(UUID id, UUID wardId) {
        this.id = Objects.requireNonNull(id, "id");
        this.wardId = Objects.requireNonNull(wardId, "wardId");
    }

    public UUID getId() {
        return id;
    }

    public UUID getWardId() {
        return wardId;
    }

    public List<Bed> getBeds() {
        return beds;
    }

    public void addBed(Bed bed) {
        beds.add(Objects.requireNonNull(bed));
    }
}
