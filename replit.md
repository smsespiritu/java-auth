# Spring Boot JWT Auth API

Spring Boot 3 REST API boilerplate for verified user accounts, JWT sessions, password recovery, and admin employee management.

## Run & Operate

- `pnpm --filter @workspace/api-server run dev` — run the Spring Boot API server (workflow-provided port, default 8080)
- `pnpm run typecheck` — full typecheck across all packages
- `pnpm --filter @workspace/api-server run build` — package the Spring Boot executable JAR
- `pnpm --filter @workspace/api-spec run codegen` — regenerate API hooks and Zod schemas from the OpenAPI spec
- `pnpm --filter @workspace/db run push` — push DB schema changes (dev only)
- Optional env: `JWT_SECRET`, `JWT_EXPIRATION_MS`, `SEED_ADMIN_ENABLED`, `SEED_ADMIN_EMAIL`, `SEED_ADMIN_PASSWORD`

## Stack

- pnpm workspaces, Node.js 24, TypeScript 5.9
- API: Spring Boot 3.3, Java 17+
- DB: H2 in-memory + Spring Data JPA
- Security: Spring Security 6, BCrypt, JJWT
- Build: Maven

## Where things live

- `artifacts/api-server/pom.xml` — Maven dependencies and Spring Boot packaging
- `artifacts/api-server/src/main/java/com/replit/authapi/model` — JPA entities and roles
- `artifacts/api-server/src/main/java/com/replit/authapi/security` — JWT filter and Spring Security configuration
- `artifacts/api-server/src/main/java/com/replit/authapi/controller` — REST endpoints and JSON exception handling
- `artifacts/api-server/src/main/resources/application.properties` — H2, JWT, and admin seed configuration

## Architecture decisions

- Accounts are disabled until the mock verification token is used.
- JWT secrets come from environment configuration; the checked-in value is development-only.
- Admin seeding is opt-in and requires an explicit password environment variable.
- Mock email delivery logs tokens through the application logger for Replit testing.

## Product

The API supports signup, email verification, login, password reset, JWT-protected routes, and an admin-only employee creation endpoint.

## User preferences

- Keep the Java API under the existing API artifact so `/api` routing remains stable.

## Gotchas

- H2 is intentionally in-memory for easy Replit setup; data resets when the service restarts.
- Admin routes require a seeded `ROLE_ADMIN` account because signup only creates `ROLE_USER`.

## Pointers

- See the `pnpm-workspace` skill for workspace structure, TypeScript setup, and package details
