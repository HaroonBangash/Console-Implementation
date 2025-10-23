package au.edu.rmit.carehome.services;

import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.medication.AuditActionType;
import au.edu.rmit.carehome.domain.medication.AuditEvent;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Design decisions: Centralised audit log writer ensures consistent payload formatting and time
 * stamping using the shared clock.
 */
public class AuditService {
    private final CareHome careHome;
    private final Clock clock;

    public AuditService(CareHome careHome, Clock clock) {
        this.careHome = careHome;
        this.clock = clock;
    }

    public AuditEvent record(Staff actor, AuditActionType actionType, String entityReference, String payload) {
        UUID actorId = actor != null ? actor.getId() : null;
        AuditEvent event = new AuditEvent(UUID.randomUUID(), actorId, actionType, entityReference,
                LocalDateTime.now(clock), payload);
        careHome.getAuditTrail().add(event);
        return event;
    }
}
