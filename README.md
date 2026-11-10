# Public Transport Ticket Booking System

A Java application, developed with **Test-Driven Development (TDD)**, that lets passengers search bus trips, book tickets and (in later phases) manage their reservations.

> **Course:** Test Driven Development — Master's in Software Engineering, Semester 3
> **Current phase:** Week 1 — requirements, repository setup and first three tested features

## What problem does it solve?

Passengers of public transport often cannot easily see which trips are available on a date, whether seats are left, and what a journey for several people costs. Operators, in turn, must avoid overbooking. This system provides the core booking logic: search trips, reserve seats safely, and calculate prices correctly.

## Main goal

The primary goal is **not only a working application but a demonstrable TDD process**: every behaviour starts as a failing test (Red), is implemented with the minimum code (Green), and is then cleaned up (Refactor). The Git history records these stages.

## Technologies

| Purpose | Technology |
|---|---|
| Language | Java 17+ |
| Build tool | Maven |
| Unit testing | JUnit 5 (Jupiter) |
| Planned later | Mockito (Week 3), Spring Boot REST API + H2 (Week 4), JaCoCo (Week 5) |
| Version control | Git + GitHub |

## Week 1 scope — three features under test

1. **Route search** — `RouteSearchService`: find trips by origin, destination and date; empty result for unknown routes; validation of invalid parameters.
2. **Ticket booking** — `BookingService`: create a booking, reject when seats are insufficient, reject invalid passenger quantities (1–10 per booking).
3. **Ticket price calculation** — `PriceCalculator`: total = unit price × passengers, rounded to 2 decimals; rejects invalid prices/quantities.

Not in scope yet: REST API, database, UI, booking cancellation (Weeks 2–6).

## Project structure

```
src/main/java/com/transport/ticketbooking/
    model/       Trip, Booking
    service/     RouteSearchService, BookingService, PriceCalculator, PassengerCount
    exception/   NoSeatsAvailableException
src/test/java/com/transport/ticketbooking/service/
    RouteSearchServiceTest, BookingServiceTest, PriceCalculatorTest
docs/
    PROJECT_PROPOSAL.md            project proposal (1–2 pages)
    REQUIREMENTS_ARCHITECTURE.md   functional requirements + architecture
    TDD_LOG.md                     Red-Green-Refactor log
    WEEK1_REPORT.md                Week 1 submission report
    evidence/                      raw test-run output for each TDD stage
```

## How to build and run the tests

Requirements: JDK 17+ and Maven 3.8+.

```bash
mvn test
```

## TDD workflow used

1. **RED** — write the test first; run it and see it fail (compilation failure counts as red).
2. **GREEN** — write the minimum production code to pass.
3. **REFACTOR** — improve structure with all tests still green.

See [`docs/TDD_LOG.md`](docs/TDD_LOG.md) and `git log` for the recorded cycles.

## Authors

- _Student name(s) — to be filled in_
