package au.edu.rmit.carehome.common;

/**
 * Design decisions: Wraps checked IO/serialization errors to keep persistence failures separate
 * from domain validation issues while remaining unchecked for concise service code.
 */
public class PersistenceException extends CareHomeException {
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
