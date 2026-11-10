# TDD Log — Week 1

Rules followed (from the course): write no production code except to pass a failing test; write only enough of a test to show a failure; write only enough code to pass. Raw outputs are in [`evidence/`](evidence/README.md) (read the note there about the runner used).

## Feature 1 — Route search (`RouteSearchService`)

| Stage | What was done | Result observed | Commit |
|---|---|---|---|
| RED 1 | Wrote `RouteSearchServiceTest` (14 tests: normal, boundary, invalid). No production code existed. | Test compilation failed (`Trip`, `RouteSearchService` missing) | `e0c2041` |
| RED 2 | Added stubs that throw `UnsupportedOperationException` so the tests compile. | 14 run, **14 failed** | `98ec870` |
| GREEN | Implemented `Trip` (fields + getters) and `RouteSearchService.search` with the simplest loop. | 14 run, **0 failed** | `6601551` |
| REFACTOR | Extracted `requireText`, `runsOnRoute`, `runsOn`; used streams; defensive copy of the timetable. | 14 run, **0 failed** | `9a30bbf` |

Test scenarios: normal (matching trips, other dates excluded, reverse direction not matched); route missing (unknown route, no trips on date); boundary (case/whitespace, 00:00 and 23:59 vs next day, empty timetable); invalid (null/blank origin, null/blank destination, null date, same origin and destination).

## Feature 2 — Ticket booking (`BookingService`)

| Stage | What was done | Result observed | Commit |
|---|---|---|---|
| RED 1 | Wrote `BookingServiceTest` (15 tests). | Test compilation failed (`Booking`, `BookingService`, `NoSeatsAvailableException`, `Trip.getAvailableSeats` missing) | `9515fc5` |
| RED 2 | Added stubs (`Booking` getters and `BookingService.book` throw `UnsupportedOperationException`). | 29 run, **15 failed** (all new), 14 route-search tests still pass | `972167c` |
| GREEN | `Trip.reserveSeats` throws `NoSeatsAvailableException` when short of seats; `BookingService.book` validates input, reserves seats, issues `BK-n` ids. | 29 run, **0 failed** | `2952d90` |
| REFACTOR | Done together with Feature 3 (see below), once duplication appeared. | — | — |

Test scenarios: normal (confirmation details, seats decrease, sequential ids); boundary (1 passenger, 10 passengers, exactly the remaining seats); no seats (sold out, one seat too many with seats unchanged, overbooking after full); invalid (0, −1, 11 passengers; null trip; null/blank passenger name).

## Feature 3 — Ticket price calculation (`PriceCalculator`)

| Stage | What was done | Result observed | Commit |
|---|---|---|---|
| RED 1 | Wrote `PriceCalculatorTest` (13 tests). | Test compilation failed (`PriceCalculator` missing) | `2ff8c9a` |
| RED 2 | Added a stub throwing `UnsupportedOperationException`. | 42 run, **13 failed** (all new) | `57fa91e` |
| GREEN | `price × passengers`, `setScale(2, HALF_UP)`, validation. | 42 run, **0 failed** | `22d7e4c` |
| REFACTOR | The 1–10 passenger rule was duplicated in `BookingService` and `PriceCalculator`; extracted to `PassengerCount.require`. | 42 run, **0 failed** | `f7c7145` |

Test scenarios: normal (1 passenger, several passengers, two-decimal result); boundary (10 passengers, price 0.01, rounding 9.999→10.00 and 0.005→0.01); invalid (null, negative and zero price; 0, −3, 11 passengers).

## Honest notes and limitations

- Red→Green was done **per feature** (the full set of tests for a feature was written first), not one test at a time. In Weeks 2–5 the cycles will be smaller.
- The Booking feature has no separate refactor commit; its duplicated validation was refactored once Feature 3 revealed the duplication.
- Results above come from the stand-in runner described in `evidence/README.md`; re-run `mvn test` for official JUnit 5/Surefire output.
- Design choices that are assumptions, not from the assignment: maximum 10 passengers per booking; unit price of 0 is rejected.
