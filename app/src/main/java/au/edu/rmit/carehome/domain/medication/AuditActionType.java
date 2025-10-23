package au.edu.rmit.carehome.domain.medication;

import java.io.Serializable;

/**
 * Design decisions: Enum enables filtering and reporting on audit events with predictable values.
 */
public enum AuditActionType implements Serializable {
    ADMIT,
    MOVE,
    DISCHARGE,
    PRESCRIBE,
    ADMINISTER,
    STAFF_UPDATE,
    LOGIN_FAILURE
}
