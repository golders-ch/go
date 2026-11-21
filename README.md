# GO

**GO** is an internal vehicle rental system for Golders.ch. Employees can find, reserve, pick up, and return company vehicles.

## Architecture

```text
Power Apps Code App
        ↓
Spring Boot API
        ↓
Microsoft Dataverse
```

All business logic is handled by the Spring Boot backend. The frontend does not access Dataverse directly.

## Technologies

### Frontend

- Power Apps Code App
- React
- TypeScript
- Vite
- Node.js and npm

### Backend

- Java 21
- Spring Boot
- Maven
- REST API
- Swagger / OpenAPI

### Platform

- Azure Container Apps
- Microsoft Dataverse
- Microsoft Entra ID
- GitHub

## Project Structure

```text
golders-go/
├── frontend/ # React / Power Apps Code App
├── backend/ # Java / Spring Boot / Maven
├── docs/ # Technical documentation
├──.gitignore
└── README.md
```

## Prerequisites

The following tools are required for local development:

- Git
- Visual Studio Code
- Java 21
- Node.js (LTS version)
- npm
- Power Apps CLI
- Access to a Power Platform development environment

## Setup

### Clone the repository

```powershell
git clone <REPOSITORY-URL>
cd golders-go
```

### Install and start the backend

The backend uses the Maven Wrapper included in the repository. A separate Maven installation is therefore usually not required.

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

The backend runs at the following address by default:

```text
http://localhost:8080
```

### Install the frontend

```powershell
cd frontend
npm install
```

The Power Apps tools only need to be installed globally once per computer:

```powershell
npm install --global @microsoft/power-apps-cli
npm install --global @microsoft/power-apps
```

If this command already outputs a version, you can skip the two installation commands:

```powershell
pa --version
```

### Initialize the Power Apps Code App

This step is only required if the project has not yet been connected to a Power Platform environment.

```powershell
pa app init --display-name "GO" --environment-id <ENVIRONMENT-ID>
```

### Start the frontend

```powershell
pa app run
```

Then open the displayed **Local Play** address in the same browser profile used to sign in to Power Apps.

## Tests

### Run backend tests

```powershell
cd backend
.\mvnw.cmd test
```

The unit tests are located in:

```text
backend/src/test/
```

The tests focus on business logic, especially:

- Validating reservation time periods
- Preventing overlapping reservations
- Checking vehicle status
- Cancelling reservations
- Vehicle pickup and return
- Allowed and disallowed status transitions

### Frontend tests

Frontend tests will be added later with Vitest and React Testing Library once the first business components and user flows have been implemented.

## Configuration

Local credentials and secrets must not be checked into the Git repository.

These include:

- Dataverse URL
- Microsoft Entra tenant ID
- Client ID
- Client Secret
- Local API URLs
- Personal access tokens

Use files such as `.env.example` or sample configurations with empty placeholders for required variables.

## Documentation

The technical documentation is located in the `docs/` directory.

Planned content:

- `architecture.md` – software architecture
- `setup-guide.md` – installation and setup guide
- `technologies.md` – technologies used
- Swagger UI – REST API documentation

## Deployment

The Spring Boot backend will later be deployed to **Azure Container Apps**. The frontend will be published as a **Power Apps Code App**.

Data is stored in Microsoft Dataverse. Backend access to Dataverse is secured using OAuth and Microsoft Entra ID.

## Project Status

The project is currently in its initial setup and planning phase.

## Project

Semester project: Vehicle rental system

Project name: **GO**

Company: **Golders.ch**
