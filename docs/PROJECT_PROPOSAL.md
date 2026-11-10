# Project Proposal — Public Transport Ticket Booking System

**Course:** Test Driven Development (TDD) — Software Engineering, Semester 3

## 1. Title

Public Transport Ticket Booking System, developed with Test-Driven Development.

## 2. Problem statement

Passengers who want to travel by bus need to know which trips exist between two places on a given day, whether seats are still free, and what the journey costs for their group. On the operator side, a booking system must never sell more seats than a vehicle has (overbooking) and must calculate prices consistently. Errors in these rules cause lost money and unhappy passengers, which makes the domain a good fit for a test-first approach: the rules are precise, have clear boundaries (seat counts, passenger limits, rounding) and are easy to express as automated tests.

## 3. Purpose and objectives

The purpose of the project is to build a small but well-engineered booking back end **and to demonstrate a systematic TDD process while doing so**. Objectives:

1. Implement the core booking rules (search, booking, seat availability, price calculation, booking management) behind a clean, testable service layer.
2. Apply Red–Green–Refactor to every behaviour, and keep the Git history as evidence.
3. Test normal, boundary and invalid-input scenarios for each feature.
4. Progress through the course phases: unit tests (Week 2), Mockito and exception tests (Week 3), REST API and integration/database tests (Week 4), JaCoCo coverage analysis (Week 5), final demonstration (Week 6).

## 4. Target users

- **Passengers** who search for trips, book tickets and later view or cancel their reservations.
- **Transport operators / ticket-office staff** who need reliable seat accounting and correct fares.
- **Course evaluators**, who need a clear, reproducible record of how the software was developed and tested.

## 5. Main functionalities

| # | Functionality | Short description | Week 1 |
|---|---|---|---|
| 1 | Route and trip search | Find trips by origin, destination and travel date | **Tested and implemented** |
| 2 | Ticket booking | Reserve seats on a trip and receive a booking confirmation | **Tested and implemented** |
| 3 | Ticket price calculation | Total price = unit price × passengers, rounded to two decimals | **Tested and implemented** |
| 4 | Seat availability | Check free seats and prevent overbooking | Basic rule included in booking; extended later |
| 5 | Booking management | View and cancel existing bookings | Planned (Week 2+) |

## 6. Technologies

Java 17+, Maven, JUnit 5 (unit testing), Mockito (Week 3), Spring Boot REST API and H2 database (Week 4), JaCoCo (Week 5), Git and GitHub.

## 7. Development approach and plan

Each behaviour is first specified by a failing JUnit test (**Red**), then implemented with the minimum code that passes (**Green**), then restructured while all tests stay green (**Refactor**). The architecture is layered (model / service / exception, with API and persistence added later) and depends on abstractions where later phases need to substitute components, which keeps the code simple to test.

| Week | Focus |
|---|---|
| 1 | Requirements, repository, README, proposal, tests and first implementation of three features |
| 2 | Core functionality with unit tests, invalid-input handling |
| 3 | Service layer, Mockito, exception handling |
| 4 | REST endpoints, integration and database tests |
| 5 | JaCoCo coverage, refactoring, final documentation |
| 6 | Final demonstration and live test execution |

## 8. Expected outcome

A working, well-tested ticket booking back end, a repository whose history shows the Red–Green–Refactor cycles, and a final test and coverage report that explains the testing decisions taken.
