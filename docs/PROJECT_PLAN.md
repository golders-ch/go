# GO Project Plan

## Project Goal

Internal vehicle rental system for Golders.ch. Employees can find, reserve,
pick up, and return company vehicles.

- Semester project
- Architecture: Power Apps Code App → Spring Boot API → Microsoft Dataverse
- All business logic is handled by the Spring Boot backend

## Current Status (2026-11-12)

- ✅ Project structure initialized (frontend, backend, docs)
- ✅ Domain model defined: `Vehicle`, `Employee`, `Reservation` (see `architecture.puml`)
- ✅ Frontend-Template eingerichtet (React 19, Vite, Tailwind, shadcn/ui, Router, React Query)
- ✅ Backend-Skeleton eingerichtet (Spring Boot, Java 21, Maven)
- ✅ **Domain model implemented** (commit `7a30e88`):
  - Entities: `Vehicle`, `Employee`, `Reservation`
  - Enums: `VehicleStatus`, `FuelType`, `Role`, `ReservationStatus`
  - Package structure: `ch.golders.backend.domain`
  - Immutability through `final` fields (except status)
- ✅ **Employee entity simplified** (only `entraId` + `role`; personal data remains in Entra ID)
- ✅ Repository interfaces designed (see `repositories.puml`)
- ✅ Repository interfaces implemented in Java (`domain`)
- ✅ In-memory repository implementations added (`infrastructure`)
- ✅ Backend startup test for loading the Spring context added
- 🔄 Reservation service started: repository injected through the constructor,
  time comparison and checks against existing reservations as private methods
- ⬜ Validate and save reservations through the service
- ⬜ Business-logic unit tests
- ⬜ REST API
- ⬜ Dataverse integration
- ⬜ Frontend still displays demo content

## Next Steps

1. **Backend: Business logic** – reservation service:
   - Validate reservation periods: start must be before end
   - Connect overlap checks with saving
   - Decide how completed reservations are handled
   - Wire the repository and service into the application
   - Check vehicle status
   - Cancellation, pickup, and return
   - Allowed/disallowed status transitions
2. **Backend: Unit tests** for business logic
3. **Backend: REST API** + Swagger/OpenAPI
4. **Dataverse integration** (persistence, OAuth via Entra ID)
5. **Frontend: Business pages** (vehicle list, reservation, etc.)
6. **Deployment** to Azure Container Apps

## Possible Enhancements (Backlog)

- Restrict vehicle rental by department (idea from the rules collection,
  intentionally not implemented for now – YAGNI)

## Milestones

| Milestone                          | Status                  |
| ---------------------------------- | ----------------------- |
| Project setup + domain model      | ✅ complete              |
| Backend: domain model             | ✅ complete (2026-10-31) |
| Backend: business logic with tests | ⬜ open                 |
| Backend: REST API                 | ⬜ open                 |
| Dataverse integration             | ⬜ open                 |
| Frontend: core features           | ⬜ open                 |
| Deployment                        | ⬜ open                 |
