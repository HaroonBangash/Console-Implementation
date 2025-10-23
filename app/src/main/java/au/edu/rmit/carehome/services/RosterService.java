package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.common.AuthorizationException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Shift;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Design decisions: Encapsulates roster modifications with compliance revalidation to keep other
 * services focused on their core workflows.
 */
public class RosterService {
    private final CareHome careHome;

    public RosterService(CareHome careHome) {
        this.careHome = careHome;
    }

    public void replaceShifts(Staff actor, List<Shift> newShifts) {
        ensureManager(actor);
        careHome.getShifts().clear();
        careHome.getShifts().addAll(newShifts);
        careHome.checkCompliance();
    }

    public void updateShiftsForStaff(Staff actor, UUID staffId, List<Shift> newShifts) {
        ensureManager(actor);
        List<Shift> remaining = careHome.getShifts().stream()
                .filter(shift -> !shift.getStaffId().equals(staffId))
                .collect(Collectors.toList());
        remaining.addAll(newShifts);
        careHome.getShifts().clear();
        careHome.getShifts().addAll(remaining);
        careHome.checkCompliance();
    }

    public boolean isOnShift(UUID staffId, LocalDateTime dateTime) {
        return careHome.getShifts().stream()
                .filter(shift -> shift.getStaffId().equals(staffId))
                .anyMatch(shift -> shift.getDayOfWeek() == dateTime.getDayOfWeek()
                        && !dateTime.toLocalTime().isBefore(shift.getStartTime())
                        && dateTime.toLocalTime().isBefore(shift.getEndTime()));
    }

    private void ensureManager(Staff actor) {
        if (actor == null || actor.getRole() != Role.MANAGER) {
            throw new AuthorizationException("Only managers can modify rosters");
        }
    }
}
