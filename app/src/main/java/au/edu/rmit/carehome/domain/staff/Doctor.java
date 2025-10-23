package au.edu.rmit.carehome.domain.staff;

import java.io.Serial;
import java.util.UUID;

/**
 * Design decisions: Concrete type to enable instanceof checks and future doctor-specific fields
 * without polluting the Staff base class.
 */
public class Doctor extends Staff {
    @Serial
    private static final long serialVersionUID = 1L;

    public Doctor(UUID id, String name, String username, String passwordHash) {
        super(id, name, Role.DOCTOR, username, passwordHash);
    }
}
