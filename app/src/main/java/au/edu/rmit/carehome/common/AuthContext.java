package au.edu.rmit.carehome.common;

import au.edu.rmit.carehome.domain.staff.Staff;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

/**
 * Design decisions: Thread-safe singleton style holder for logged-in staff used by UI; serializes
 * with the rest of the state for graceful resume of last session.
 */
public class AuthContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Staff currentStaff;

    public Optional<Staff> currentUser() {
        return Optional.ofNullable(currentStaff);
    }

    public void login(Staff staff) {
        this.currentStaff = staff;
    }

    public void logout() {
        this.currentStaff = null;
    }
}
