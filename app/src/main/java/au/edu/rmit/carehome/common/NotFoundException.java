package au.edu.rmit.carehome.common;

/**
 * Design decisions: Provides a descriptive signal when domain objects are missing so callers can
 * translate to user-friendly messaging and auditing.
 */
public class NotFoundException extends CareHomeException {
    public NotFoundException(String message) {
        super(message);
    }
}
