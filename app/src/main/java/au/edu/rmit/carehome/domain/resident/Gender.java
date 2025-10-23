package au.edu.rmit.carehome.domain.resident;

import java.io.Serializable;

/**
 * Design decisions: Enum encapsulating binary gender to drive bed segregation and UI colour
 * coding; kept serializable for persistence.
 */
public enum Gender implements Serializable {
    MALE,
    FEMALE
}
