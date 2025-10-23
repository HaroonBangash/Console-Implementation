# Resident HealthCare System

## Overview
This project implements a resident management platform for an aged-care facility. Two wards are
modelled, each containing rooms and beds, and the JavaFX UI lets clinical staff see occupancy,
update medication workflows, and manage rosters. The system serialises its full in-memory state on
exit and can archive discharged residents to JSON for auditing.

## Features
- Role-based authorisation for managers, doctors, and nurses.
- Ward/room/bed map with colour-coded gender indicators.
- Admission, transfer, discharge workflows with audit logging.
- Prescription authoring and medication administration tracking.
- Roster compliance validation (nurse double-shift pattern, doctor daily coverage).
- Persistence via binary serialisation and JSON archive exports.
- Deterministic sample data loader (1 manager, 1 doctor, 6 nurses, residents, and schedules).

## Getting Started
### Prerequisites
- Java 17+ JDK with JavaFX modules available on the module/classpath.
- Gradle 8+ (or use the supplied wrapper once dependencies are reachable).
- The Gradle wrapper JAR is generated on demand from a checked-in Base64 payload so
  the repository can remain binary-free for PR tooling; no manual action is required.

If the runtime cannot download dependencies (e.g., Maven Central blocked) place the JavaFX and
Jackson libraries on a local Maven repository or update `app/build.gradle` with direct file
references.

### Build & Test
```
# Run unit tests
gradle -p app test
```

### Run the Application
```
# Launch the JavaFX UI
gradle -p app run
```

The application stores serialised state under `data/carehome.ser` and archives discharged residents
under `data/archive/`.

### Seed Credentials
Sample credentials installed by `SampleDataSeeder`:
- Manager: `manager` / `manager123`
- Doctor: `doctor` / `doctor123`
- Nurses: `nurse1`..`nurse6` / matching password (`nurse1`, etc.)

## Project Layout
- `app/src/main/java/au/edu/rmit/carehome/app`: bootstrap, DI container, sample data.
- `domain`: entity model (residents, staff, facility, medication, compliance rules).
- `services`: business services enforcing authorisation, roster checks, and audits.
- `persistence`: serializer and JSON archiver abstraction.
- `ui`: JavaFX presenter implementing the ward map and role actions.
- `tests`: JUnit 5 coverage of business rules and persistence round-trip.

## Notes
- Password hashing uses salted SHA-256 for simplicity; swap to PBKDF2/BCrypt for production.
- Compliance checks mirror the assignment specification and will raise descriptive exceptions for
violations.
