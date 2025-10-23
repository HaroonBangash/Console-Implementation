package au.edu.rmit.carehome.domain.facility;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Ward groups rooms for UI sections and policy enforcement; contains list to
 * avoid repeated lookups when rendering.
 */
public class Ward implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String name;
    private final List<Room> rooms = new ArrayList<>();

    public Ward(UUID id, String name) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void addRoom(Room room) {
        rooms.add(Objects.requireNonNull(room));
    }
}
