package au.edu.rmit.carehome.domain.resident;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Design decisions: Resident aggregates immutable identity data with mutable bed assignment and
 * timeline history entries for quick audit context; history is stored as structured strings for
 * persistence simplicity while full medication events live in dedicated collections.
 */
public class Resident implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private String name;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Set<String> conditionFlags;
    private UUID bedId;
    private final List<String> historyEntries;

    public Resident(UUID id, String name, Gender gender, LocalDate dateOfBirth, Set<String> conditionFlags) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.gender = Objects.requireNonNull(gender, "gender");
        this.dateOfBirth = Objects.requireNonNull(dateOfBirth, "dateOfBirth");
        this.conditionFlags = Objects.requireNonNull(conditionFlags, "conditionFlags");
        this.historyEntries = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Set<String> getConditionFlags() {
        return conditionFlags;
    }

    public void setConditionFlags(Set<String> conditionFlags) {
        this.conditionFlags = conditionFlags;
    }

    public UUID getBedId() {
        return bedId;
    }

    public void setBedId(UUID bedId) {
        this.bedId = bedId;
    }

    public List<String> getHistoryEntries() {
        return historyEntries;
    }

    public void addHistoryEntry(String entry) {
        this.historyEntries.add(entry);
    }
}
