# Spring Data JPA - Auditing & Specification (Live Demo)

Spring Boot 3.5 · Java 25 · Gradle (Groovy) · YAML config · Lombok · PostgreSQL 17 (Docker)

## Run

Requires Docker (Desktop or Engine) running.

```bash
# one-time: generate the Gradle wrapper (needs Gradle 9.1+ installed)
gradle wrapper --gradle-version 9.1.0

./gradlew bootRun      # Spring Boot auto-starts docker-compose.yml (Postgres) for you
./gradlew test         # tests use in-memory H2, no Docker needed
```

Or start the database yourself first:

```bash
docker compose up -d           # start Postgres (+ pgAdmin)
docker compose ps              # check it is healthy
docker compose down            # stop (data kept in the volume)
docker compose down -v         # stop AND wipe all data
```

| Service | URL / port | Credentials |
|---|---|---|
| API | http://localhost:8080 | - |
| PostgreSQL | `localhost:5433` (container listens on 5432 internally), db `demo_db` | `demo_user` / `demo_pass` |
| pgAdmin | http://localhost:5050 | no login; add server host `postgres`, port `5432`, user/pass above |

Quick look at the audit columns without pgAdmin:

```bash
docker exec -it jpa-demo-postgres psql -U demo_user -d demo_db \
  -c "select id, name, created_by, created_date, last_modified_by, last_modified_date from students;"
```

**Port already in use?** The host port defaults to `5433` because `5432` is often taken by a local Postgres.
Pick another one with `DB_PORT=5434 docker compose up -d` and run the app with the same `DB_PORT`.
To see what holds a port: `lsof -i :5433` (macOS/Linux) or `netstat -ano | findstr 5433` (Windows).

Connection settings live in `application.yml` and can be overridden with env vars:
`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`.

## Map: slides -> code

| Slide topic | File |
|---|---|
| 7.2 `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy` | `entity/BaseEntity.java` |
| 7.3 Enabling auditing (`@EnableJpaAuditing`, `AuditorAware`) | `config/AuditorConfig.java` |
| 7.4 Reusable `@MappedSuperclass` | `BaseEntity` -> `Student`, `Product`, `Category` |
| 8.1 Why Specification (derived-query explosion) | `repository/StudentRepository.java`, `StudentService.searchWithDerivedQueries` |
| 8.2 Criteria API (`CriteriaBuilder`, `CriteriaQuery`, `Root`, `Predicate`) | `service/CriteriaApiDemoService.java` |
| 8.3 `Specification` interface | `specification/ProductSpecification.categoryEquals` |
| 8.4 Dynamic queries / best practice | `specification/StudentSpecification.java` |

## Suggested live-demo flow (about 10 min)

1. **Auditing**: `requests.http` #1 shows the four audit fields. #2 with `X-User: alice`, then #3 with `X-User: bob`: `createdBy` stays `alice`, `lastModifiedBy` becomes `bob`, `lastModifiedDate` moves. #5 shows `Category` gets the same columns for free.
2. **Why Specification**: show the six derived methods in `StudentRepository`, then request #8. Ask: "what happens with 5 filters?" (31 methods).
3. **Criteria API**: #6 and #7. Watch the SQL in the console.
4. **Specification**: #9 (simplest), #10-#12 (dynamic - watch the `WHERE` clause appear and disappear in the console), #13 (composition + paging + sorting), #14.
5. **Tests**: `./gradlew test`.

## Notes

* Audit user comes from the `X-User` header; no header -> `admin` (as in the slide); seeder/tests -> `system`.
* Two small fixes versus the slides so the code compiles: `root.<String>get("name")` before `cb.lower(...)` and `root.<Integer>get("age")` before comparisons.
* `createdDate` / `createdBy` are `updatable = false` so they can never be overwritten.
* The slide-style `search(...)` and the composed `Specification.allOf(...)` version return the same results.
