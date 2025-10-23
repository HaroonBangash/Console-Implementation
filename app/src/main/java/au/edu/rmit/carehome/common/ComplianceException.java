package au.edu.rmit.carehome.common;

/**
 * Design decisions: Dedicated exception for regulatory roster rules so service layers can surface
 * actionable compliance feedback when schedules are invalid.
 */
public class ComplianceException extends CareHomeException {
    public ComplianceException(String message) {
        super(message);
    }
}
