# Fitness Microservice Platform

A fitness tracking backend built using a microservices architecture, with two independently deployable Spring Boot services: one for user accounts and one for activity tracking.

## Overview

| Service | Responsibility | Database | Port |
|---|---|---|---|
| `userservice` | User registration, profile retrieval | PostgreSQL (via JPA/Hibernate) | `8081` |
| `activityservice` | Logging and retrieving workout activities | MongoDB (via Spring Data MongoDB) | `8082` |

## Why two different databases?

This project uses **polyglot persistence** — each service uses the database best suited to its data shape, rather than forcing one database on the whole system:

- **User data** is structured and relational (unique emails, fixed roles, predictable fields), which fits a SQL database like PostgreSQL well.
- **Activity data** varies per workout type — a running session and a weight-training session don't share the same metrics — which fits MongoDB's flexible document model better.

Each service owns its database exclusively. Neither service queries the other's database directly; this keeps them independently deployable and scalable.

## Tech Stack

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA** (Hibernate) — `userservice`
- **Spring Data MongoDB** — `activityservice`
- **PostgreSQL** — `userservice`'s database
- **MongoDB** — `activityservice`'s database
- **Lombok** — reduces boilerplate (getters/setters/constructors)
- **Jakarta Bean Validation** — request validation (`@NotBlank`, `@Email`, etc.)
- **Maven** — build tool

## Architecture

```
Client (Postman / Frontend)
       │
       ├──► POST /api/users/register    ──► userservice (8081) ──► PostgreSQL
       ├──► GET  /api/users/{userId}    ──► userservice (8081) ──► PostgreSQL
       │
       └──► POST /api/activities        ──► activityservice (8082) ──► MongoDB
```

Each service follows a layered structure:

```
Controller  →  Service  →  Repository  →  Database
   (HTTP)      (logic)      (data access)
```

- **Controller** — receives HTTP requests, delegates to the service layer, returns HTTP responses.
- **Service** — business logic (e.g. checking for duplicate emails before registering a user).
- **Repository** — a Spring Data interface; the actual database queries are generated automatically.
- **Model/Entity** — maps directly to how data is stored in the database.
- **DTO (Data Transfer Object)** — controls exactly what data goes in and out of the API, separate from the database shape.

## API Endpoints

### userservice (`http://localhost:8081`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users/register` | Register a new user |
| `GET` | `/api/users/{userId}` | Get a user's profile by ID |

**Register request body:**
```json
{
  "email": "user@example.com",
  "password": "yourpassword",
  "firstName": "John",
  "lastName": "Doe"
}
```

### activityservice (`http://localhost:8082`)

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/activities` | Log a new fitness activity |

**Track activity request body:**
```json
{
  "userId": "u123",
  "type": "RUNNING",
  "duration": 30,
  "caloriesBurned": 300,
  "startTime": "2026-10-11T07:00:00",
  "additionalMetrics": { "distanceKm": 5.2 }
}
```

## Running Locally

### Prerequisites
- Java 21
- Maven (or use the included `mvnw` wrapper)
- MongoDB running locally on `localhost:27017`
- PostgreSQL running locally on `localhost:5432`, with a database named `fitness_user_db`

### 1. Clone the repo
```bash
git clone https://github.com/AtharvaD-18/fitness-microservice.git
cd fitness-microservice
```

### 2. Set the database password environment variable
`userservice` reads its PostgreSQL password from an environment variable rather than storing it in `application.yml`.

**In IntelliJ/VS Code:** set `DB_PASSWORD` in the run configuration's environment variables.

**In a terminal (PowerShell):**
```powershell
$env:DB_PASSWORD="your_postgres_password"
```

### 3. Run each service
In separate terminals:
```bash
cd activityservice
./mvnw spring-boot:run
```
```bash
cd userservice
./mvnw spring-boot:run
```

- `activityservice` starts on `http://localhost:8082`
- `userservice` starts on `http://localhost:8081`

## Known Limitations / Roadmap

This is an active learning project. Current known items being worked on:

- [ ] Hash passwords before storing (currently stored in plain text — not production-ready)
- [ ] Remove password field from API responses
- [ ] Add JWT-based authentication between services
- [ ] Add an API Gateway to route requests through a single entry point
- [ ] Add Docker Compose for one-command local startup of both services + databases

## Author

Built by [Atharva Dudhwadkar](https://github.com/AtharvaD-18) as a learning project exploring Spring Boot microservices architecture.