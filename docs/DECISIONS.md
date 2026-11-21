# Architecture Decision Log

## 2026-11-12 – Provisional reservations block time periods

**Decision:** Provisional reservations block a vehicle's time period. Cancelled
reservations do not block it.

**Context:** The status of existing reservations must be considered when
checking for overlaps.

**Alternative:** Only confirmed reservations block a time period.

**Rationale:** A vehicle must not be reserved elsewhere while a provisional
reservation exists. How completed reservations are handled is still open; they
are currently included in the check.

## 2026-11-12 – Allow reservations that start immediately after another

**Decision:** A reservation may start exactly when the previous one ends.
Time periods are treated as half-open for overlap checks: the start is included,
the end is excluded.

**Context:** Vehicle availability checks are being developed in the reservation
service. The handling of adjacent time periods must be unambiguous.

**Alternative:** Require a break between reservations.

**Rationale:** Vehicles can be reserved again immediately without a required
break. Any preparation time is therefore not included.

## 2026-11-09 – Availability checks belong in the service, not the repository

**Decision:** The check for whether a vehicle is available during a time period
(`checkAvailability`) is **not** part of the `ReservationRepository` interface;
it is business logic in `ReservationService` (package `application`).

**Context:** During the design of the repository interfaces (`repositories.puml`),
`checkAvailability` was initially placed in the repository. Repositories should
only encapsulate persistence operations (loading/saving), not business logic.

**Alternatives:** Keep `checkAvailability` in the repository (mixes persistence
with business logic); add a dedicated query method such as
`findOverlapping(...)` to the repository (could be useful with Dataverse later,
but YAGNI for now).

**Rationale:** The service retrieves reservations with `findByVehicle(vehicle)`
and checks for overlaps itself. This keeps the rule in one place, makes it
testable without a database, and keeps the repository interface small. A query
method can be added later if performance requires it, without duplicating the
rule.

## 2026-11-09 – Keep the employee entity minimal (entraId + role)

**Decision:** `Employee` contains only `entraId` and `role`. Personal data
(first name, last name, date of birth, department, etc.) is not stored in the
backend.

**Context:** Identity and personal data are already maintained in Microsoft
Entra ID. Duplicating them in the backend would require synchronization and
would unnecessarily store personal data in another location.

**Alternatives:** A complete employee profile in the backend (with
synchronization issues); a partial profile (display name only) – both rejected.

**Rationale:** Single source of truth (Entra ID), data minimization, and less
code. The backend needs the Entra ID for authentication and the role for
authorization – nothing more.

## 2026-10-31 – Lightweight Clean Architecture package structure

**Decision:** The backend is divided into four packages:
`domain` (entities, enums, repository interfaces), `application` (services and
business logic), `infrastructure` (in-memory repositories, later Dataverse),
and `api` (REST controllers).

**Context:** Dataverse persistence will be added later, but business logic
should be implemented and tested now.

**Alternatives:** A flat structure (everything in one package); a conventional
controller-service-repository structure without an explicit domain package.

**Rationale:** Dependencies point inward toward `domain`. When switching to
Dataverse later, only `infrastructure` will be replaced; business logic and the
API remain unchanged. The structure also stays manageable for a semester
project.

## 2026-10-31 – Add ACTIVE and COMPLETED reservation statuses

**Decision:** Add `ACTIVE` and `COMPLETED` to the reservation statuses.
Lifecycle: `PROVISIONAL → CONFIRMED → ACTIVE → COMPLETED`, with `CANCELLED`
possible at any time before pickup.

**Context:** The README requires business logic for vehicle pickup and return.
The original model (`PROVISIONAL`, `CONFIRMED`, `CANCELLED`) could not represent
these operations.

**Alternatives:** Represent pickup only through the vehicle status (`RENTED`);
omit pickup and return for now.

**Rationale:** An explicit lifecycle makes the allowed status transitions
clearly defined and easy to test – the exact focus of the backend tests
described in the README.

## 2026-11-01 – Use LocalDateTime for reservation periods

**Decision:** Change `startDate`/`endDate` in `Reservation` from `LocalDate` to
`LocalDateTime` and rename them to `startDateTime`/`endDateTime`.

**Context:** Employees need vehicles for only a few hours in some use cases
(for example, a morning customer visit). At this point, there is no persistence,
API, or frontend, so this change is nearly free now and would be expensive
later.

**Alternatives:** Day-based reservations with `LocalDate` (simpler, but no
half-day bookings); `Instant`/`ZonedDateTime` (time zone support – overkill for
a single-location Swiss company).

**Rationale:** The overlap logic is structurally identical for both types; the
additional work is limited to frontend input and tests. The business value
justifies it. `LocalDateTime` correctly represents a time period as local time
without time zone complexity.
