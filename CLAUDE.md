# Google Calendar v3 Connector — CLAUDE.md

## Project Overview

**Artifact:** `org.bonitasoft.connectors:bonita-connector-google-calendar-v3`
**Current version:** `1.1.2-SNAPSHOT`
**License:** GPL v2.0
**Java:** 11 (compiled with `maven.compiler.release=11`)

This is a Bonita BPM connector that integrates Google Calendar API v3 into Bonita process automation. It provides five operations as distinct connector implementations:

| Connector class | Operation |
|---|---|
| `CreateEventConnector` | Insert a new calendar event |
| `GetEventConnector` | Retrieve an existing event |
| `UpdateEventConnector` | Patch/update an existing event |
| `DeleteEventConnector` | Delete an event |
| `MoveEventConnector` | Move an event to a different calendar |

Authentication supports both JSON service account tokens and legacy P12 key files.

## Build Commands

```bash
# Full build (compile + test + package + jacoco coverage)
./mvnw clean verify

# Build with SonarCloud analysis (requires SONAR_TOKEN env var)
./mvnw -B -ntp clean verify sonar:sonar

# Skip tests (not recommended)
./mvnw clean package -DskipTests

# Run only tests
./mvnw test

# Deploy to Maven Central (requires GPG key + OSSRH credentials, deploy profile)
./mvnw clean deploy -P deploy
```

The default Maven goal is `verify`. The Maven wrapper (`mvnw` / `mvnw.cmd`) should always be used rather than a local Maven installation.

## Architecture

### Class Hierarchy

```
AbstractConnector  (Bonita engine)
  └── CalendarConnector          — auth, HTTP transport setup, common I/O constants
        ├── BuildEventConnector  — date/time parsing, event field mapping (create/update)
        │     ├── CreateEventConnector
        │     └── UpdateEventConnector
        ├── GetEventConnector
        ├── DeleteEventConnector
        └── MoveEventConnector
```

### Key packages

- `org.bonitasoft.connectors.google.calendar` — concrete connector implementations
- `org.bonitasoft.connectors.google.calendar.common` — abstract base classes shared across connectors

### Connector descriptor resources

Each connector has a `.def` (definition) and `.impl` (implementation) XML file under `src/main/resources-filtered/`. Maven property filtering injects version numbers at build time. `.properties` files in `src/main/resources/` provide i18n labels (en, fr, es, it, pt).

### Assembly

`src/assembly/` contains assembly descriptors that produce per-connector ZIP archives (plus an `all` aggregate ZIP) during the `package` phase.

### Authentication modes

- **JSON** — `ServiceAccountCredentials` loaded from a JSON token string; recommended modern approach.
- **P12** — Legacy `GoogleCredential` builder with a P12 file path on disk.

Both modes require a service account ID, a target calendar ID, and optionally a delegated service account user.

## Testing

Tests live in `src/test/java/org/bonitasoft/connectors/google/calendar/`.

- Framework: JUnit 5 (`junit-jupiter-engine 5.9.2`)
- Assertions: AssertJ (`assertj-core 3.23.1`)
- Mocking: Mockito (`mockito-core 4.6.1`)
- Coverage: JaCoCo (reports generated at `verify` phase, appended across unit + integration runs)

### Running tests

```bash
./mvnw test          # unit tests only
./mvnw verify        # unit + integration tests + coverage report
```

Test helper `DefaultConnectorConfiguration` (in `common` test package) provides reusable connector input map setup for use across test classes.

## Commit Format

This repository follows **Conventional Commits**:

```
<type>(<scope>): <subject>
```

Common types: `feat`, `fix`, `chore`, `refactor`, `test`, `docs`, `ci`, `build`.

Scope examples: `create-event`, `get-event`, `update-event`, `delete-event`, `move-event`, `ci`, `deps`.

A `commit-message-check.yml` GitHub Actions workflow enforces this format on every PR.

Examples:
```
feat(create-event): support recurring event recurrence rules
fix(auth): handle empty service account user for JSON auth mode
chore(deps): bump google-api-services-calendar to v3-rev20230701-2.0.0
ci: add Claude Code review workflow
```

## Release Process

Releases are triggered via the **Release** GitHub Actions workflow (`release.yml`) using `workflow_dispatch` with a version input (e.g. `1.1.2`).

The workflow delegates to the reusable `_reusable_release_connector.yml` workflow from `bonitasoft/github-workflows@main`, which typically:
1. Sets the Maven release version and removes `-SNAPSHOT`
2. Builds and signs artifacts (GPG, `deploy` profile)
3. Publishes to Maven Central via `central-publishing-maven-plugin`
4. Creates a GitHub release/tag

To prepare a release locally for testing:
```bash
./mvnw versions:set -DnewVersion=1.1.2 -DgenerateBackupPoms=false
./mvnw clean verify
```
