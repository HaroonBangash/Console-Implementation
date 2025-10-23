package au.edu.rmit.carehome.domain.core;

import au.edu.rmit.carehome.common.ComplianceException;
import au.edu.rmit.carehome.domain.facility.Bed;
import au.edu.rmit.carehome.domain.facility.Room;
import au.edu.rmit.carehome.domain.facility.Ward;
import au.edu.rmit.carehome.domain.medication.AuditEvent;
import au.edu.rmit.carehome.domain.medication.MedicationAdministration;
import au.edu.rmit.carehome.domain.medication.Prescription;
import au.edu.rmit.carehome.domain.resident.Resident;
import au.edu.rmit.carehome.domain.rules.CareHomeCompliance;
import au.edu.rmit.carehome.domain.staff.Shift;
import au.edu.rmit.carehome.domain.staff.Staff;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Design decisions: Aggregate root that stores all mutable collections, enabling persistence and
 * services to share a single in-memory source of truth. Utility finders centralize lookups.
 */
public class CareHome implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Ward> wards = new ArrayList<>();
    private final Map<UUID, Resident> residents = new HashMap<>();
    private final Map<UUID, Staff> staff = new HashMap<>();
    private final List<Shift> shifts = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<MedicationAdministration> administrations = new ArrayList<>();
    private final List<AuditEvent> auditTrail = new ArrayList<>();

    public List<Ward> getWards() {
        return wards;
    }

    public Map<UUID, Resident> getResidents() {
        return residents;
    }

    public Map<UUID, Staff> getStaff() {
        return staff;
    }

    public List<Shift> getShifts() {
        return shifts;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public List<MedicationAdministration> getAdministrations() {
        return administrations;
    }

    public List<AuditEvent> getAuditTrail() {
        return auditTrail;
    }

    public Optional<Bed> findBed(UUID bedId) {
        return wards.stream()
                .flatMap(ward -> ward.getRooms().stream())
                .flatMap(room -> room.getBeds().stream())
                .filter(bed -> bed.getId().equals(bedId))
                .findFirst();
    }

    public Optional<Room> findRoom(UUID roomId) {
        return wards.stream()
                .flatMap(ward -> ward.getRooms().stream())
                .filter(room -> room.getId().equals(roomId))
                .findFirst();
    }

    public Optional<Ward> findWard(UUID wardId) {
        return wards.stream().filter(ward -> ward.getId().equals(wardId)).findFirst();
    }

    public void checkCompliance() {
        CareHomeCompliance.validate(this);
    }

    public List<Bed> getAllBeds() {
        return wards.stream()
                .flatMap(ward -> ward.getRooms().stream())
                .flatMap(room -> room.getBeds().stream())
                .collect(Collectors.toList());
    }

    public List<Resident> getResidentsForWard(UUID wardId) {
        return residents.values().stream()
                .filter(resident -> resident.getBedId() != null)
                .filter(resident -> findBed(resident.getBedId())
                        .flatMap(bed -> findRoom(bed.getRoomId()))
                        .map(room -> room.getWardId().equals(wardId))
                        .orElse(false))
                .collect(Collectors.toList());
    }
}
