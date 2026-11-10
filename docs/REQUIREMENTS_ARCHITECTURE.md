# Functional Requirements and Initial Architecture

## 1. Functional requirements

Priority uses MoSCoW (Must / Should / Could). "Status" refers to the end of Week 1.

| ID | Requirement | Priority | Acceptance criteria (summary) | Status |
|---|---|---|---|---|
| FR-1 | Search trips by origin, destination and date | Must | Returns all trips on that route and date; empty list if none; origin/destination match ignoring case and surrounding spaces; reverse direction is not a match | Done (Week 1) |
| FR-1.1 | Validate search parameters | Must | Null/blank origin or destination, null date, or identical origin and destination are rejected with `IllegalArgumentException` | Done |
| FR-2 | Book seats on a trip | Must | Valid booking returns a confirmation with id (`BK-n`), trip, passenger name, seats; available seats decrease | Done |
| FR-2.1 | Prevent overbooking | Must | Requesting more seats than are available throws `NoSeatsAvailableException` and leaves seat count unchanged | Done |
| FR-2.2 | Validate booking input | Must | Passengers must be 1–10; trip must exist; passenger name must not be blank | Done |
| FR-3 | Calculate ticket price | Must | Total = unit price × passengers, rounded half-up to 2 decimals | Done |
| FR-3.1 | Validate price input | Must | Unit price must be positive and not null; passengers 1–10 | Done |
| FR-4 | View bookings | Should | A passenger can list existing bookings | Week 2+ |
| FR-5 | Cancel a booking | Should | Cancelling restores the seats to the trip | Week 2+ |
| FR-6 | Expose functionality through a REST API | Should | Endpoints for search, booking, cancel | Week 4 |
| FR-7 | Persist data in a database (H2) | Should | Trips and bookings stored and retrievable | Week 4 |
| FR-8 | Group discounts, payments, user accounts | Won't (for now) | — | Out of scope |

### Non-functional requirements

- **Testability:** every rule is covered by automated tests; services have no hidden global state.
- **Maintainability:** small classes with a single responsibility; business rules defined in one place (e.g. `PassengerCount`).
- **Correctness of money:** `BigDecimal` is used for prices, never `double`.

### Business rules / assumptions

- A booking may contain 1 to 10 passengers (`PassengerCount.MIN`/`MAX`).
- Prices are positive (a unit price of 0 is rejected) and use two decimal places, half-up rounding.
- A trip has a single origin–destination pair and one departure date-time.
- In Week 1 the timetable is an in-memory list; no database yet.

## 2. Initial architecture

A layered, package-by-layer structure. Only the layers marked ✔ exist in Week 1.

```
 ┌───────────────────────────────────────────────┐
 │  API layer (REST controllers)          Week 4 │
 ├───────────────────────────────────────────────┤
 │  Service layer ✔                              │
 │    RouteSearchService  BookingService         │
 │    PriceCalculator     PassengerCount         │
 ├───────────────────────────────────────────────┤
 │  Domain model ✔                               │
 │    Trip  Booking   NoSeatsAvailableException  │
 ├───────────────────────────────────────────────┤
 │  Persistence (repositories, H2)        Week 4 │
 └───────────────────────────────────────────────┘
```

| Class | Responsibility |
|---|---|
| `Trip` | A scheduled trip: id, origin, destination, departure, available seats, unit price. Owns the seat-reservation rule (`reserveSeats`). |
| `Booking` | Immutable booking confirmation (id, trip, passenger, seats). |
| `RouteSearchService` | Filters the timetable by route and date; validates input. |
| `BookingService` | Validates a request, reserves seats, issues sequential booking ids. |
| `PriceCalculator` | Computes the total price with correct rounding. |
| `PassengerCount` | Package-private helper holding the single 1–10 passenger rule (introduced by refactoring). |
| `NoSeatsAvailableException` | Signals that a booking exceeds the available seats. |

### Design decisions

- **Plain Java first, Spring later.** Week 1 has no framework; services are POJOs that can be unit-tested directly. Spring Boot is added in Week 4 without changing the domain rules.
- **Exceptions for invalid use.** `IllegalArgumentException` for bad input; a dedicated `NoSeatsAvailableException` for the business failure "sold out".
- **Simplicity over anticipation.** Features such as cancellation, repositories and interfaces for Mockito are intentionally *not* built yet; the tests demand them in later weeks.

## 3. Test strategy (Week 1)

- Unit tests with JUnit 5 for each service; one test class per feature.
- For each feature: normal cases, boundary values, invalid input.
- Boundary examples: 1 and 10 passengers (valid) vs 0 and 11 (invalid); booking exactly the remaining seats vs one more; trips at 00:00 / 23:59 vs the next day; price rounding at 0.005 and 9.999.
- Test names are written as behaviour descriptions (`@DisplayName`) so a test report reads as a specification.
