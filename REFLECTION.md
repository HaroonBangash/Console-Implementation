# Reflection

Implementing the Resident HealthCare System reinforced the value of isolating business logic from UI
code. The most challenging part was honouring the roster-compliance rules while keeping services
agnostic about persistence. Introducing `CareHome` as an aggregate root allowed every service to
share the same collections without a complex repository layer, and the `CareHomeCompliance`
validator keeps compliance checks reusable and testable.

Authorisation proved tricky because role and roster checks both gate actions. Injecting a shared
`Clock` made it straightforward to reproduce off-shift failures in unit tests, and the audit trail
keeps diagnostics consistent. For persistence I leaned on Java serialisation for snapshots and a JSON
archiver for discharge exports; the abstraction ensures a database-backed implementation can be
added later without touching core logic.

With more time I would flesh out the UI with richer filtering, implement bulk roster editing instead
of the minimal manager dialogs, and replace the password hashing with PBKDF2 or BCrypt. Packaging a
self-contained JavaFX runtime or bundling dependencies would also improve portability in restricted
network environments.
