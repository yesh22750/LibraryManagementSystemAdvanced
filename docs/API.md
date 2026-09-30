# REST API

Base URL: `http://localhost:8080`

- `GET /api/health` — health check
- `GET /api/books` — list books
- `GET /api/books?q=clean` — search by title
- `GET /api/books/{id}` — get one book
- `POST /api/books` — create book
- `DELETE /api/books/{id}` — delete book
- `/swagger-ui.html` — interactive API documentation

The current starter intentionally keeps write endpoints open for local learning. Add authentication before any real deployment.
