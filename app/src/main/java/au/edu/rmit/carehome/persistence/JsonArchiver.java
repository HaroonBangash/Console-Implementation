package au.edu.rmit.carehome.persistence;

import au.edu.rmit.carehome.common.PersistenceException;
import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.medication.MedicationAdministration;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.resident.Resident;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Design decisions: Serialises discharged resident data into JSON for external audit consumption.
 */
public class JsonArchiver implements Archiver {
    private final Path archiveDirectory;
    private final ObjectMapper mapper;

    public JsonArchiver(Path archiveDirectory) {
        this.archiveDirectory = archiveDirectory;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    @Override
    public void archiveResident(CareHome careHome, Resident resident) {
        try {
            Files.createDirectories(archiveDirectory);
            Path file = archiveDirectory.resolve(resident.getId() + "-archive.json");
            List<Prescription> prescriptions = careHome.getPrescriptions().stream()
                    .filter(prescription -> prescription.getResidentId().equals(resident.getId()))
                    .collect(Collectors.toList());
            Map<UUID, List<MedicationAdministration>> administrations = careHome.getAdministrations().stream()
                    .filter(administration -> administration.getResidentId().equals(resident.getId()))
                    .collect(Collectors.groupingBy(MedicationAdministration::getPrescriptionItemId));

            ArchivePayload payload = new ArchivePayload(resident, prescriptions, administrations);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), payload);
        } catch (Exception e) {
            throw new PersistenceException("Failed to archive resident", e);
        }
    }

    /**
     * Design decisions: Simple DTO for JSON serialisation.
     */
    public record ArchivePayload(Resident resident, List<Prescription> prescriptions,
                                 Map<UUID, List<MedicationAdministration>> administrations) {
    }
}
