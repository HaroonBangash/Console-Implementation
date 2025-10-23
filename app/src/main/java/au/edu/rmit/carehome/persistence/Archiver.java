package au.edu.rmit.carehome.persistence;

import au.edu.rmit.carehome.domain.core.CareHome;
import au.edu.rmit.carehome.domain.resident.Resident;

/**
 * Design decisions: Allows discharge workflow to plug in different archival strategies (JSON or DB).
 */
public interface Archiver {
    void archiveResident(CareHome careHome, Resident resident);
}
