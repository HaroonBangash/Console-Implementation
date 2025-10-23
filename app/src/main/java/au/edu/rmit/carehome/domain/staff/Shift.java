package au.edu.rmit.carehome.domain.staff;

import java.io.Serial;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Shift value object captures roster slots for compliance validation and
 * on-shift checks.
 */
public class Shift implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID staffId;
    private final DayOfWeek dayOfWeek;
    private final LocalTime startTime;
    private final LocalTime endTime;

    public Shift(UUID staffId, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.staffId = Objects.requireNonNull(staffId, "staffId");
        this.dayOfWeek = Objects.requireNonNull(dayOfWeek, "dayOfWeek");
        this.startTime = Objects.requireNonNull(startTime, "startTime");
        this.endTime = Objects.requireNonNull(endTime, "endTime");
    }

    public UUID getStaffId() {
        return staffId;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
