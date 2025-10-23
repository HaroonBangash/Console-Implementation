package au.edu.rmit.carehome.common;

import java.io.Serial;
import java.io.Serializable;
import java.time.Clock;

/**
 * Design decisions: Wraps a pluggable Clock to decouple time from system defaults so tests can
 * inject deterministic instants.
 */
public final class ClockProvider implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Clock clock;

    public ClockProvider(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }
}
