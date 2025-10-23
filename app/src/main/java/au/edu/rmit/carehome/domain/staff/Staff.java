package au.edu.rmit.carehome.domain.staff;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Abstracts shared staff identity and credentials while allowing role-specific
 * subtypes to exist for future extensions; serializable for persistence snapshots.
 */
public class Staff implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String name;
    private final Role role;
    private String username;
    private String passwordHash;
    private boolean active = true;

    public Staff(UUID id, String name, Role role, String username, String passwordHash) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.role = Objects.requireNonNull(role, "role");
        this.username = Objects.requireNonNull(username, "username");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
