# Design Notes

## Architectural Overview
The system follows a layered architecture:

- **Domain Layer** models residents, staff, facility structure, medication artefacts, and compliance
  rules. `CareHome` acts as the aggregate root for serialisation and service coordination.
- **Service Layer** (`services` package) encapsulates business workflows such as admissions,
  medication, staff management, and rosters. Each service takes dependencies on the domain model,
  the shared audit logger, and supporting infrastructure.
- **Persistence Layer** provides a `Serializer` abstraction (binary Java serialisation for snapshot
  storage) and an `Archiver` abstraction (JSON exports for discharged residents). Implementations are
  easily swappable thanks to the interface boundaries.
- **UI Layer** (`MainView`) is a JavaFX presenter that binds toolbar actions to service calls while
  rendering the ward/room/bed topology.
- **Bootstrap Layer** wires dependencies together, loads persisted state, and injects deterministic
  sample data when no snapshot is found.

## Key Design Decisions
- **Design Comments** at the top of each class justify responsibilities and trade-offs as per the
  assignment brief.
- **Singleton-like Container**: `CareHomeContainer` exposes configured services and persistence
  objects to the UI, avoiding a heavy DI framework.
- **Clock Injection**: A shared `Clock` instance allows tests to simulate on/off-shift behaviour and
  ensures audit timestamps are deterministic in unit tests.
- **Audit Logging**: All state-changing services delegate to `AuditService` to centralise logging and
  guarantee consistent audit payloads.
- **Roster Compliance**: `CareHome.checkCompliance()` delegates to `CareHomeCompliance` to keep the
  aggregate lean while ensuring schedule validation is reusable across services and tests.
- **Sample Data**: `SampleDataSeeder` populates the minimum viable configuration (2 wards, shifts,
  staff, residents) to satisfy functional demos and provide predictable credentials.

## Error Handling Strategy
Domain-specific exceptions (`AuthorizationException`, `ValidationException`, `ComplianceException`,
`NotFoundException`) are unchecked to keep service code succinct while allowing the UI to present
clear messages. `PersistenceException` wraps IO issues to prevent leaking implementation details.

## Extensibility
Future enhancements (e.g., more shift patterns, alternate persistence) can be layered onto the
existing abstractions without touching UI code thanks to the aggregate/service separation. JSON
archiving already accommodates switching to a database by providing the `Archiver` interface.
