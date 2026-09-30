# Library Management System — Ultimate Full-Stack Starter

A portfolio-ready library platform architecture:

**HTML/CSS/JavaScript (frontend) → Spring Boot REST API → MySQL**

The project combines a normalized relational database with a Java/Spring Boot backend, Flyway migrations, JPA repositories, validation, Actuator health checks, OpenAPI/Swagger documentation, CORS configuration and a responsive browser frontend.

## Tech Stack
- Java 21
- Spring Boot 3.5.6
- Spring Web / Spring Data JPA
- Spring Security foundation
- MySQL 8+
- Flyway migrations
- OpenAPI / Swagger UI
- HTML5 / CSS3 / JavaScript
- Maven

## Run in IntelliJ
1. Open the `backend` folder as a Maven project.
2. Make sure MySQL is running.
3. Set `DB_USERNAME` and `DB_PASSWORD` if your MySQL credentials differ from `root/root`.
4. Run `LibraryManagementApplication`.
5. Open `http://localhost:8080/swagger-ui.html`.
6. Serve the `frontend` folder using IntelliJ's built-in/static server or VS Code Live Server.

## Database
Flyway creates the `library_management` schema and starter data automatically. The migration files are versioned and should not be edited after being applied in a shared environment; add a new migration instead.

## Portfolio scope
The database layer is the foundation for books, members and transactions. The REST API exposes the book catalog and health endpoint. The frontend consumes the API. The architecture is ready for reservations, authentication, admin roles, notifications, analytics, React, Docker and cloud deployment.

## Important
This repository is a strong local development/portfolio foundation, not a production-ready security system. Before deployment, implement authentication, authorization, secret management, rate limiting, audit controls and HTTPS.
