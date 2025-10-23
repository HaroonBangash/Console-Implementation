package au.edu.rmit.carehome.domain.medication;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Design decisions: Represents a specific medicine regimen, identified by id so administrations
 * can reference the exact schedule entry.
 */
public class PrescriptionItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final String drugName;
    private final double dose;
    private final String unit;
    private final List<LocalTime> scheduleTimes = new ArrayList<>();

    public PrescriptionItem(UUID id, String drugName, double dose, String unit, List<LocalTime> scheduleTimes) {
        this.id = Objects.requireNonNull(id, "id");
        this.drugName = Objects.requireNonNull(drugName, "drugName");
        this.dose = dose;
        this.unit = Objects.requireNonNull(unit, "unit");
        this.scheduleTimes.addAll(scheduleTimes);
    }

    public UUID getId() {
        return id;
    }

    public String getDrugName() {
        return drugName;
    }

    public double getDose() {
        return dose;
    }

    public String getUnit() {
        return unit;
    }

    public List<LocalTime> getScheduleTimes() {
        return scheduleTimes;
    }
}
