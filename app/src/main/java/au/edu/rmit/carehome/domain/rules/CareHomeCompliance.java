package au.edu.rmit.carehome.domain.rules;

import au.edu.rmit.carehome.common.ComplianceException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.staff.Role;
import au.edu.rmit.carehome.domain.staff.Shift;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Design decisions: Static validator centralises regulatory checks so services can reuse a single
 * entry point and tests target a focused class.
 */
public final class CareHomeCompliance {
    private static final LocalTime NURSE_SHIFT_ONE_START = LocalTime.of(8, 0);
    private static final LocalTime NURSE_SHIFT_ONE_END = LocalTime.of(16, 0);
    private static final LocalTime NURSE_SHIFT_TWO_START = LocalTime.of(14, 0);
    private static final LocalTime NURSE_SHIFT_TWO_END = LocalTime.of(22, 0);

    private CareHomeCompliance() {
    }

    public static void validate(CareHome careHome) {
        Map<UUID, Staff> staff = careHome.getStaff();
        List<Shift> shifts = careHome.getShifts();

        Map<DayOfWeek, List<Shift>> nurseByDay = shifts.stream()
                .filter(shift -> roleOf(staff, shift.getStaffId()) == Role.NURSE)
                .collect(Collectors.groupingBy(Shift::getDayOfWeek, () -> new EnumMap<>(DayOfWeek.class), Collectors.toList()));

        for (DayOfWeek day : DayOfWeek.values()) {
            List<Shift> daily = nurseByDay.getOrDefault(day, List.of());
            if (daily.size() != 2) {
                throw new ComplianceException("Nurse roster must have exactly two shifts on " + day);
            }
            boolean hasFirst = daily.stream().anyMatch(shift -> shift.getStartTime().equals(NURSE_SHIFT_ONE_START)
                    && shift.getEndTime().equals(NURSE_SHIFT_ONE_END));
            boolean hasSecond = daily.stream().anyMatch(shift -> shift.getStartTime().equals(NURSE_SHIFT_TWO_START)
                    && shift.getEndTime().equals(NURSE_SHIFT_TWO_END));
            if (!hasFirst || !hasSecond) {
                throw new ComplianceException("Nurse shifts on " + day + " must be 08-16 and 14-22");
            }
        }

        Map<UUID, Map<DayOfWeek, Duration>> nurseHours = shifts.stream()
                .filter(shift -> roleOf(staff, shift.getStaffId()) == Role.NURSE)
                .collect(Collectors.groupingBy(Shift::getStaffId,
                        Collectors.groupingBy(Shift::getDayOfWeek, () -> new EnumMap<>(DayOfWeek.class),
                                Collectors.reducing(Duration.ZERO, CareHomeCompliance::durationOf, Duration::plus))));

        nurseHours.forEach((nurseId, durations) -> durations.forEach((day, duration) -> {
            if (duration.compareTo(Duration.ofHours(8)) > 0) {
                String name = staff.get(nurseId).getName();
                throw new ComplianceException("Nurse " + name + " exceeds 8 hours on " + day);
            }
        }));

        Map<DayOfWeek, Duration> doctorCoverage = shifts.stream()
                .filter(shift -> roleOf(staff, shift.getStaffId()) == Role.DOCTOR)
                .collect(Collectors.groupingBy(Shift::getDayOfWeek, () -> new EnumMap<>(DayOfWeek.class),
                        Collectors.reducing(Duration.ZERO, CareHomeCompliance::durationOf, Duration::plus)));

        for (DayOfWeek day : DayOfWeek.values()) {
            Duration coverage = doctorCoverage.getOrDefault(day, Duration.ZERO);
            if (coverage.compareTo(Duration.ofHours(1)) < 0) {
                throw new ComplianceException("Doctor coverage less than 1 hour on " + day);
            }
        }
    }

    private static Duration durationOf(Shift shift) {
        return Duration.between(shift.getStartTime(), shift.getEndTime());
    }

    private static Role roleOf(Map<UUID, Staff> staff, UUID staffId) {
        Staff member = staff.get(staffId);
        if (member == null) {
            throw new ComplianceException("Shift assigned to unknown staff " + staffId);
        }
        return member.getRole();
    }
}
