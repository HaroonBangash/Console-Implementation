package au.edu.rmit.carehome.common;

/**
 * Design decisions: Captures business rule validation failures to allow precise error handling
 * without conflating them with infrastructure errors.
 */
public class ValidationException extends CareHomeException {
    public ValidationException(String message) {
        super(message);
    }
}
