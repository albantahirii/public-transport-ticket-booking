# Project rules for Claude — Public Transport Ticket Booking System (TDD course)

Act as a senior Java engineer and TDD mentor. Work phase by phase (Weeks 1–6, see README and docs/); never build ahead of the current week.

## TDD rules (strict)
- Never write production code before a failing test for it exists.
- Follow Red -> Green -> Refactor for every behaviour: write only enough test to fail, only enough code to pass, then refactor with all tests green.
- Cover normal, boundary and invalid-input cases for each feature.
- Keep the design simple, SOLID and testable; do not generate the whole application at once.

## Git workflow
- One commit per TDD stage, using prefixes: `test(<feature>): RED ...`, `feat(<feature>): GREEN ...`, `refactor(<feature>): ...`, `docs: ...`.
- Run the tests (`mvn test`) before every GREEN/REFACTOR commit; never claim a result that was not actually executed.
- After each green or refactor stage: `git push origin main`.

## Phase plan
1. Requirements, repo, proposal, first tests (done)
2. Core functionality with unit tests
3. Service layer, Mockito, exception tests
4. REST API, integration and database tests (Spring Boot, H2)
5. JaCoCo coverage, refactoring, final documentation
6. Final demo with live test execution

## Documentation
- Keep `docs/TDD_LOG.md` updated with each Red/Green/Refactor cycle and the observed test results.
- Reports are written in Albanian (`*_SQ.md`) and English; keep them consistent.
