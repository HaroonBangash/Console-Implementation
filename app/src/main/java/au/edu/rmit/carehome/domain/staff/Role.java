package au.edu.rmit.carehome.domain.staff;

import java.io.Serializable;

/**
 * Design decisions: Enumerates the discrete staff roles used in authorization logic and UI gating.
 */
public enum Role implements Serializable {
    MANAGER,
    DOCTOR,
    NURSE
}
