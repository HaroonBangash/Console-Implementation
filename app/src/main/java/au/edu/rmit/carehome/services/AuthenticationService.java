package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.common.PasswordHasher;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.medication.AuditActionType;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.util.Optional;

/**
 * Design decisions: Handles credential verification and audit logging for logins.
 */
public class AuthenticationService {
    private final CareHome careHome;
    private final AuditService auditService;

    public AuthenticationService(CareHome careHome, AuditService auditService) {
        this.careHome = careHome;
        this.auditService = auditService;
    }

    public Staff login(String username, String password) {
        Optional<Staff> match = careHome.getStaff().values().stream()
                .filter(staff -> staff.getUsername().equalsIgnoreCase(username))
                .findFirst();
        Staff staff = match.orElseThrow(() -> new AuthorizationException("Invalid credentials"));
        if (!PasswordHasher.matches(password, staff.getPasswordHash())) {
            auditService.record(staff, AuditActionType.LOGIN_FAILURE, staff.getId().toString(), "Invalid password");
            throw new AuthorizationException("Invalid credentials");
        }
        return staff;
    }
}
