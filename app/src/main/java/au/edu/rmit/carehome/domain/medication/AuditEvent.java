package au.edu.rmit.carehome.domain.medication;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Captures actor, action, and payload for regulatory auditing; stored centrally
 * in CareHome aggregate.
 */
public class AuditEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final UUID actorStaffId;
    private final AuditActionType actionType;
    private final String entityReference;
    private final LocalDateTime timestamp;
    private final String payload;

    public AuditEvent(UUID id, UUID actorStaffId, AuditActionType actionType, String entityReference,
                      LocalDateTime timestamp, String payload) {
        this.id = Objects.requireNonNull(id, "id");
        this.actorStaffId = actorStaffId;
        this.actionType = Objects.requireNonNull(actionType, "actionType");
        this.entityReference = entityReference;
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp");
        this.payload = payload;
    }

    public UUID getId() {
        return id;
    }

    public UUID getActorStaffId() {
        return actorStaffId;
    }

    public AuditActionType getActionType() {
        return actionType;
    }

    public String getEntityReference() {
        return entityReference;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getPayload() {
        return payload;
    }
}
