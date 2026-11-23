# GO

**GO** is a semester project for an internal vehicle rental system at
Golders.ch. Employees will be able to find, reserve, pick up, and return company
vehicles.

## Current project direction

The application is being developed locally. The reduced-scope target
architecture is:

```text
Power Apps Code App (local) → Spring Boot backend (local) → H2 database
```

The original plan was to connect the Power Apps Code App to a Spring Boot API,
store data in Microsoft Dataverse, deploy the backend to Azure Container Apps,
and use Microsoft Entra ID. The expected implementation effort was too large
for the project, so the scope was reduced to local execution with H2. Azure
deployment, Dataverse, and Entra ID integration are not part of the current
scope. The employee model retains an `entraId` field so Entra ID can be
integrated later if needed.

**Implementation status:** The domain model, reservation service, in-memory
repositories, and reservation tests are present. The REST API and H2
persistence are planned but not implemented yet; the frontend still contains
template/demo content.

## Technologies

### Frontend

- Power Apps Code App, intended to run locally
- React 19, TypeScript, and Vite
- Tailwind CSS and shadcn/ui
- React Router, TanStack Query, and Zustand

### Backend

- Java 21
- Spring Boot
- Maven
- H2 database (planned)

## Project structure

```text
go/
├── frontend/ # React / Vite application
├── backend/  # Java / Spring Boot / Maven application
├── docs/     # Project plan, decisions, learning log, and diagrams
└── README.md
```

## Prerequisites

- Git
- Java 21
- Node.js (LTS) and npm

## Local development

### Backend

Start the Spring Boot application:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The application listens at `http://localhost:8080` by default. Until the REST
API and H2 persistence are implemented, the available backend functionality is
limited to the current in-memory implementation.

Run backend tests:

```powershell
cd backend
.\mvnw.cmd test
```

### Frontend

Install dependencies and start the local Power Apps Code App:

```powershell
cd frontend
npm install
pa app run
```

Frontend tests have not been added yet. Azure deployment and publishing to a
Power Platform environment are outside the current project scope.

## Documentation

Project documentation is in [`docs/`](docs/):

- [`PROJECT_PLAN.md`](docs/PROJECT_PLAN.md) — scope, status, and next steps
- [`DECISIONS.md`](docs/DECISIONS.md) — architectural decisions and changes
- [`LEARNING_LOG.md`](docs/LEARNING_LOG.md) — learning notes
- PlantUML diagrams — domain model, repositories, and backend overview
