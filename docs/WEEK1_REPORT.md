# Week 1 Submission Report

# Public Transport Ticket Booking System

**Course:** Test Driven Development (TDD) — Software Engineering, Semester 3
**Student:** Alban Tahiri
**GitHub repository:** https://github.com/albantahirii/public-transport-ticket-booking

## 1. Project description

The project is a Java back end for booking public-transport (bus) tickets. A passenger searches trips by origin, destination and date, books seats on a trip, and pays a price calculated from the number of passengers. The system prevents overbooking and rejects invalid input. The aim of the course project is to show a systematic Test-Driven Development process, so every behaviour starts as a failing JUnit 5 test.

Week 1 covers requirements, repository setup, the project proposal and the first three tested features. No REST API, database or user interface has been built yet (these belong to later weeks).

## 2. Project proposal (summary)

The full 1–2 page proposal is in `docs/PROJECT_PROPOSAL.md`.

- **Problem:** passengers need reliable information on trips, seat availability and fares; operators must avoid overbooking and price errors.
- **Objectives:** implement the core booking rules behind a clean service layer; apply Red–Green–Refactor to every behaviour; test normal, boundary and invalid inputs; keep a Git history that proves the process.
- **Users:** passengers, ticket-office staff / operators, course evaluators.
- **Main functionalities:** (1) route and trip search, (2) ticket booking, (3) ticket price calculation, (4) seat availability / overbooking prevention, (5) booking management (view, cancel — later weeks).
- **Technologies:** Java 17+, Maven, JUnit 5; later Mockito, Spring Boot, H2, JaCoCo; Git and GitHub.

Requirements and architecture are documented in `docs/REQUIREMENTS_ARCHITECTURE.md` (layered design: model, service, exception; API and persistence planned for Week 4).

## 3. Repository

Layout (see README.md in the repository):

```
pom.xml, README.md, .gitignore
src/main/java/com/transport/ticketbooking/{model,service,exception}
src/test/java/com/transport/ticketbooking/service
docs/  (proposal, requirements/architecture, TDD log, this report, evidence/)
```

The repository (https://github.com/albantahirii/public-transport-ticket-booking) contains the full commit history recording the Red–Green–Refactor stages (see section 6).

## 4. The three functionalities selected for testing

### 4.1 Route search — `RouteSearchService.search(origin, destination, date)`

Returns the trips that run from origin to destination on the given date.

- Normal: finds all matching trips; excludes other dates; reverse direction is not a match.
- Route does not exist: returns an empty list.
- Boundary: case-insensitive and whitespace-tolerant matching; trips at 00:00 and 23:59 are included, 00:00 of the next day is not; empty timetable.
- Invalid: null/blank origin, null/blank destination, null date, identical origin and destination → `IllegalArgumentException`.

### 4.2 Ticket booking — `BookingService.book(trip, passengerName, seats)`

Creates a `Booking` (id `BK-n`), reduces the available seats.

- Normal: confirmation contains trip, passenger and seat count; seats decrease; ids are sequential.
- Boundary: 1 passenger, 10 passengers, exactly the remaining seats.
- No seats: sold-out trip, one seat more than available (seat count unchanged), second booking after the trip is full → `NoSeatsAvailableException`.
- Invalid: 0, −1 or 11 passengers; null trip; null/blank passenger name → `IllegalArgumentException`.

### 4.3 Ticket price calculation — `PriceCalculator.calculateTotal(unitPrice, passengers)`

Total = unit price × passengers, rounded half-up to two decimals (`BigDecimal`).

- Normal: 1 passenger (10.00), several passengers (12.50 × 3 = 37.50), two-decimal result.
- Boundary: 10 passengers, smallest price 0.01, rounding 3.333 × 3 = 9.999 → 10.00 and 0.005 → 0.01.
- Invalid: null, negative or zero price; 0, −3 or 11 passengers.

Example of a test written first (JUnit 5):

```java
@Test
@DisplayName("rejects a booking for one seat more than available and leaves seats unchanged")
void rejectsOneSeatTooMany() {
    Trip trip = tripWithSeats(3);

    assertThrows(NoSeatsAvailableException.class, () -> service.book(trip, "Alban", 4));

    assertEquals(3, trip.getAvailableSeats());
}
```

## 5. Test summary

| Test class | Tests | Normal | Boundary | Invalid / failure |
|---|---|---|---|---|
| `RouteSearchServiceTest` | 14 | 3 | 3 (+2 no-result cases) | 6 |
| `BookingServiceTest` | 15 | 3 | 3 | 3 sold-out/overbooking + 6 invalid |
| `PriceCalculatorTest` | 13 | 3 | 4 | 6 |
| **Total** | **42** | | | |

## 6. Red–Green–Refactor evidence and test execution results

Full log: `docs/TDD_LOG.md`. Raw outputs: `docs/evidence/`.

| Feature | RED (tests only) | RED (stubs compile) | GREEN | REFACTOR |
|---|---|---|---|---|
| Route search | does not compile | 14 run, 14 failed | 14 run, 0 failed | 14 run, 0 failed |
| Booking | does not compile | 15 of 15 new tests failed (29 run) | 29 run, 0 failed | (with shared refactor) |
| Price calculation | does not compile | 13 of 13 new tests failed (42 run) | 42 run, 0 failed | 42 run, 0 failed |

Final result at the last commit: **Tests run: 42, Failures: 0** (RouteSearchServiceTest 14, BookingServiceTest 15, PriceCalculatorTest 13).

**Execution environment.** The tests were executed with `javac` and a minimal JUnit-compatible runner because Maven Central was not reachable in the development sandbox. The test sources are unchanged and target the JUnit 5 API; the pass/fail results reflect real execution of the production code, but the console format differs from Maven Surefire. The official run is `mvn test` (expected: Tests run: 42, Failures: 0, Errors: 0, Skipped: 0); see `docs/evidence/README.md`.

## 7. Conclusion and next steps

Week 1 is complete: the project is defined, the repository structure, README and proposal are prepared, and three functionalities were developed test-first with normal, boundary and invalid-input scenarios. Planned for Week 2: core functionality (booking management and seat availability), more invalid-input handling, and smaller Red–Green–Refactor steps.
