package au.edu.rmit.carehome.common;

import java.io.Serial;

/**
 * Design decisions: Base unchecked exception so domain/services can throw rich errors without
 * forcing callers to catch; keeps messages actionable for UI and tests.
 */
public class CareHomeException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CareHomeException(String message) {
        super(message);
    }

    public CareHomeException(String message, Throwable cause) {
        super(message, cause);
    }
}
