package au.edu.rmit.carehome.domain.staff;

import java.io.Serial;
import java.util.UUID;

/**
 * Design decisions: Simple subtype for clarity when enforcing nurse-only actions.
 */
public class Nurse extends Staff {
    @Serial
    private static final long serialVersionUID = 1L;

    public Nurse(UUID id, String name, String username, String passwordHash) {
        super(id, name, Role.NURSE, username, passwordHash);
    }
}
