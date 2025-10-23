package au.edu.rmit.carehome.domain.staff;

import java.io.Serial;
import java.util.UUID;

/**
 * Design decisions: Manager subtype makes authorization checks self-documenting in code and tests.
 */
public class Manager extends Staff {
    @Serial
    private static final long serialVersionUID = 1L;

    public Manager(UUID id, String name, String username, String passwordHash) {
        super(id, name, Role.MANAGER, username, passwordHash);
    }
}
