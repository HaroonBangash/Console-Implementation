package au.edu.rmit.carehome.common;

/**
 * Design decisions: Specialized exception for authz/authn failures enabling the UI to display
 * role or shift issues distinctly from validation errors.
 */
public class AuthorizationException extends CareHomeException {
    public AuthorizationException(String message) {
        super(message);
    }
}
