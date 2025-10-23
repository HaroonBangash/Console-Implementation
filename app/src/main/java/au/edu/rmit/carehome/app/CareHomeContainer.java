package au.edu.rmit.carehome.app;

import au.edu.rmit.carehome.common.AuthContext;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.persistence.Archiver;
import au.edu.rmit.carehome.persistence.Serializer;
import au.edu.rmit.carehome.services.AdmissionService;
import au.edu.rmit.carehome.services.AuthenticationService;
import au.edu.rmit.carehome.services.AuditService;
import au.edu.rmit.carehome.services.MedicationService;
import au.edu.rmit.carehome.services.RosterService;
import au.edu.rmit.carehome.services.StaffService;

import java.time.Clock;

/**
 * Design decisions: Simple dependency container exposed to UI controllers for service access and
 * persistence on shutdown.
 */
public class CareHomeContainer {
    private final CareHome careHome;
    private final Serializer serializer;
    private final Archiver archiver;
    private final Clock clock;
    private final AuthContext authContext;
    private final AuditService auditService;
    private final StaffService staffService;
    private final RosterService rosterService;
    private final AdmissionService admissionService;
    private final MedicationService medicationService;
    private final AuthenticationService authenticationService;

    public CareHomeContainer(CareHome careHome, Serializer serializer, Archiver archiver, Clock clock,
                             AuthContext authContext, AuditService auditService, StaffService staffService,
                             RosterService rosterService, AdmissionService admissionService,
                             MedicationService medicationService, AuthenticationService authenticationService) {
        this.careHome = careHome;
        this.serializer = serializer;
        this.archiver = archiver;
        this.clock = clock;
        this.authContext = authContext;
        this.auditService = auditService;
        this.staffService = staffService;
        this.rosterService = rosterService;
        this.admissionService = admissionService;
        this.medicationService = medicationService;
        this.authenticationService = authenticationService;
    }

    public CareHome getCareHome() {
        return careHome;
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public Archiver getArchiver() {
        return archiver;
    }

    public Clock getClock() {
        return clock;
    }

    public AuthContext getAuthContext() {
        return authContext;
    }

    public AuditService getAuditService() {
        return auditService;
    }

    public StaffService getStaffService() {
        return staffService;
    }

    public RosterService getRosterService() {
        return rosterService;
    }

    public AdmissionService getAdmissionService() {
        return admissionService;
    }

    public MedicationService getMedicationService() {
        return medicationService;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
