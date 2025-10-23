package au.edu.rmit.carehome.app;

import au.edu.rmit.carehome.common.AuthContext;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.persistence.Archiver;
import au.edu.rmit.carehome.persistence.FileSerializer;
import au.edu.rmit.carehome.persistence.JsonArchiver;
import au.edu.rmit.carehome.persistence.Serializer;
import au.edu.rmit.carehome.services.AdmissionService;
import au.edu.rmit.carehome.services.AuthenticationService;
import au.edu.rmit.carehome.services.AuditService;
import au.edu.rmit.carehome.services.MedicationService;
import au.edu.rmit.carehome.services.RosterService;
import au.edu.rmit.carehome.services.StaffService;

import java.nio.file.Path;
import java.time.Clock;

/**
 * Design decisions: Bootstraps persistence, services, and seed data into a container consumed by
 * the JavaFX application.
 */
public class CareHomeBootstrap {
    private final Path dataFile;
    private final Path archiveDirectory;

    public CareHomeBootstrap(Path dataFile, Path archiveDirectory) {
        this.dataFile = dataFile;
        this.archiveDirectory = archiveDirectory;
    }

    public CareHomeContainer initialise() {
        Serializer serializer = new FileSerializer();
        CareHome careHome = serializer.load(dataFile);
        Clock clock = Clock.systemDefaultZone();
        AuthContext authContext = new AuthContext();
        Archiver archiver = new JsonArchiver(archiveDirectory);
        AuditService auditService = new AuditService(careHome, clock);
        StaffService staffService = new StaffService(careHome, auditService);
        RosterService rosterService = new RosterService(careHome);
        AdmissionService admissionService = new AdmissionService(careHome, auditService, clock, archiver);
        MedicationService medicationService = new MedicationService(careHome, auditService, rosterService, clock);
        AuthenticationService authenticationService = new AuthenticationService(careHome, auditService);

        if (careHome.getWards().isEmpty()) {
            SampleDataSeeder.seed(careHome);
        }

        return new CareHomeContainer(careHome, serializer, archiver, clock, authContext, auditService,
                staffService, rosterService, admissionService, medicationService, authenticationService);
    }

    public void persist(CareHomeContainer container) {
        container.getSerializer().save(container.getCareHome(), dataFile);
    }
}
