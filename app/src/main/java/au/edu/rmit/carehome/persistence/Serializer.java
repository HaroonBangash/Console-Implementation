package au.edu.rmit.carehome.persistence;

import au.edu.rmit.carehome.domain.core.CareHome;

import java.nio.file.Path;

/**
 * Design decisions: Abstraction allows future DB or network persistence without changing services.
 */
public interface Serializer {
    void save(CareHome careHome, Path path);

    CareHome load(Path path);
}
