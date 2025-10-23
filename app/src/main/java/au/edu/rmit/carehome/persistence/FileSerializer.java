package au.edu.rmit.carehome.persistence;

import au.edu.rmit.carehome.common.PersistenceException;
import au.edu.rmit.carehome.domain.core.CareHome;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Design decisions: Java serialization keeps Phase-1 persistence simple while maintaining the
 * Serializer abstraction for swapping to a database later.
 */
public class FileSerializer implements Serializer {
    @Override
    public void save(CareHome careHome, Path path) {
        try {
            Files.createDirectories(path.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
                out.writeObject(careHome);
            }
        } catch (Exception e) {
            throw new PersistenceException("Failed to save care home state", e);
        }
    }

    @Override
    public CareHome load(Path path) {
        if (!Files.exists(path)) {
            return new CareHome();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (CareHome) in.readObject();
        } catch (Exception e) {
            throw new PersistenceException("Failed to load care home state", e);
        }
    }
}
