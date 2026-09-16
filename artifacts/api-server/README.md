# Spring Boot JWT Auth API

This service is a Java 17+ Spring Boot REST API with stateless JWT authentication, H2 persistence, email verification, password reset, and an admin-only employee endpoint.

## Run on Replit

The existing API workflow runs:

```bash
pnpm --filter @workspace/api-server run dev
```

The service listens on the workflow-provided `PORT` (default `8080`) and exposes:

- `GET /api/healthz`
- `POST /api/auth/signup`
- `GET /api/auth/verify-email?token=...`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/reset-password`
- `POST /api/admin/employees` (requires `ROLE_ADMIN`)

## Authentication flow

1. Sign up with an email and password. The account is disabled until verified.
2. Copy the verification token from the Replit console and call `GET /api/auth/verify-email?token=...`.
3. Log in to receive a Bearer JWT.
4. Send the token with `Authorization: Bearer <token>` when calling protected endpoints.

Password reset tokens are also printed to the console by the mock email service.

## Admin testing

To seed an enabled admin account for local testing, set:

```bash
SEED_ADMIN_ENABLED=true
SEED_ADMIN_EMAIL=admin@example.com
SEED_ADMIN_PASSWORD='use-a-local-test-password'
```

Admin seeding is disabled by default. Provide `JWT_SECRET` in production as a base64-encoded secret of at least 256 bits.

## Example requests

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H 'Content-Type: application/json' \
  -d '{"email":"user@example.com","password":"password123"}'

curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"admin@example.com","password":"use-a-local-test-password"}'

curl -X POST http://localhost:8080/api/admin/employees \
  -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer <access-token>' \
  -d '{"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com","department":"Engineering"}'
```