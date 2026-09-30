# Architecture

```text
Browser (HTML/CSS/JavaScript or future React)
                |
                v
       Spring Boot REST API
       Controller -> Service -> Repository
                |
                v
             MySQL
                |
             Flyway
```

## Extension roadmap
- JWT/OAuth2 authentication and role-based access
- reservations and waitlists
- email/SMS reminders
- pagination, sorting and advanced search
- Redis caching
- Docker Compose
- CI/CD
- React or Next.js frontend
- metrics/observability
- cloud deployment
