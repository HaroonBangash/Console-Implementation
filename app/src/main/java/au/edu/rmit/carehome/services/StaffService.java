package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.common.NotFoundException;
import au.edu.rmit.carehome.common.ValidationException;
import au.edu.rmit.carehome.common.PasswordHasher;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.medication.AuditActionType;
import au.edu.rmit.carehome.domain.staff.Doctor;
import au.edu.rmit.carehome.domain.staff.Manager;
import au.edu.rmit.carehome.domain.staff.Nurse;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Design decisions: Enforces manager-only credential operations and centralises staff lifecycle
 * state changes to keep authentication code lean.
 */
public class StaffService {
    private final CareHome careHome;
    private final AuditService auditService;

    public StaffService(CareHome careHome, AuditService auditService) {
        this.careHome = careHome;
        this.auditService = auditService;
    }

    public Staff createStaff(Staff actor, Role role, String name, String username, String password) {
        ensureManager(actor);
        ensureUniqueUsername(username);
        Staff staff = instantiate(role, name, username, password);
        careHome.getStaff().put(staff.getId(), staff);
        auditService.record(actor, AuditActionType.STAFF_UPDATE, staff.getId().toString(), "Created staff");
        return staff;
    }

    public void updateCredentials(Staff actor, UUID staffId, String username, String password) {
        ensureManager(actor);
        Staff staff = findStaff(staffId);
        if (!staff.getUsername().equals(username)) {
            ensureUniqueUsername(username);
            staff.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            staff.setPasswordHash(PasswordHasher.hash(password));
        }
        auditService.record(actor, AuditActionType.STAFF_UPDATE, staffId.toString(), "Updated credentials");
    }

    public List<Staff> getStaffByRole(Role role) {
        return careHome.getStaff().values().stream()
                .filter(member -> member.getRole() == role)
                .collect(Collectors.toList());
    }

    public Staff findStaff(UUID staffId) {
        Staff staff = careHome.getStaff().get(staffId);
        if (staff == null) {
            throw new NotFoundException("Staff not found");
        }
        return staff;
    }

    private void ensureManager(Staff actor) {
        if (actor == null || actor.getRole() != Role.MANAGER) {
            throw new AuthorizationException("Only managers can manage staff");
        }
    }

    private void ensureUniqueUsername(String username) {
        boolean exists = careHome.getStaff().values().stream()
                .anyMatch(member -> member.getUsername().equalsIgnoreCase(username));
        if (exists) {
            throw new ValidationException("Username already exists");
        }
    }

    private Staff instantiate(Role role, String name, String username, String password) {
        String hash = PasswordHasher.hash(password);
        UUID id = UUID.randomUUID();
        return switch (Objects.requireNonNull(role)) {
            case MANAGER -> new Manager(id, name, username, hash);
            case DOCTOR -> new Doctor(id, name, username, hash);
            case NURSE -> new Nurse(id, name, username, hash);
        };
    }
}
