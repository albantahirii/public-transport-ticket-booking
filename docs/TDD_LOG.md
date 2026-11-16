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
- Per-stage results above were captured with the stand-in runner described in `evidence/README.md`; the final state was confirmed by an official `mvn test` run (42 tests, 0 failures, BUILD SUCCESS) in `evidence/mvn-test-official.txt`.
- Design choices that are assumptions, not from the assignment: maximum 10 passengers per booking; unit price of 0 is rejected.

---

# Java 3 — Shënime në shqip (pikat 1–7)

## Funksionaliteti i ri: anulimi i rezervimit (FR-5)

`BookingService.cancel(bookingId)` e shënon rezervimin si të anuluar dhe ia kthen ulëset udhëtimit.

| Faza | Çfarë u bë | Rezultati (`mvn clean test`) | Commit |
|---|---|---|---|
| RED | U shkrua `BookingCancellationTest` (5 teste) para kodit. | `COMPILATION ERROR`: `cancel` dhe `isCancelled` nuk ekzistojnë | `59ebaaf` |
| GREEN | `Booking.cancel()`/`isCancelled()`, `Trip.releaseSeats()`, `BookingService.cancel()`. | 47 teste, 0 dështime | `7ee15aa` |
| REFACTOR | Tri testet për numër të pavlefshëm pasagjerësh u bashkuan në një `@ParameterizedTest`. | 47 teste, 0 dështime | `6a97680` |
| TEST | Vlerat kufitare 1, 2, 9, 10 me `@CsvSource`. | 49 teste, 0 dështime | `3022fd6` |

## Si plotësohen pikat e detyrës

| Pika | Ku gjendet |
|---|---|
| 1. Kodi burimor | `model/` (`Trip`, `Booking`), `service/` (`RouteSearchService`, `BookingService`, `PriceCalculator`, `PassengerCount`), `exception/` |
| 2. Testet JUnit 5 | Një klasë testuese për çdo funksionalitet: `RouteSearchServiceTest`, `BookingServiceTest`, `PriceCalculatorTest`, `BookingCancellationTest` |
| 3. Pozitive / negative | Çdo klasë testuese ka seksionet *positive/normal* dhe *negative/invalid* |
| 4. Vlerat kufitare | Pasagjerët: 0, 1, 2, 9, 10, 11. Ulëset: saktësisht sa ka dhe +1. Ora: 00:00 dhe 23:59. Çmimi: 0.01 |
| 5. Branch testing | Shih tabelën më poshtë |
| 6. `@ParameterizedTest` | `PriceCalculatorTest`: `rejectsInvalidPassengerCount` (`@ValueSource`) dhe `acceptsPassengerCountsFromMinToMax` (`@CsvSource`) |
| 7. Procesi TDD | Commit-et `test(...): RED` → `feat(...): GREEN` → `refactor(...)` në `git log` (java 1 dhe java 3) |

## Branch testing: çdo degë e kushteve dhe testi që e ekzekuton

| Kushti në kod | Dega `true` | Dega `false` |
|---|---|---|
| `Trip.reserveSeats`: `seats > availableSeats` | `rejectsOneSeatTooMany`, `rejectsWhenSoldOut` | `bookingExactlyRemainingSeats` |
| `PassengerCount.require`: `< 1` ose `> 10` | `rejectsInvalidPassengerCount` (-3, 0, 11) | `acceptsPassengerCountsFromMinToMax` (1, 2, 9, 10) |
| `PriceCalculator`: çmimi `null` ose `<= 0` | `rejectsNullPrice`, `rejectsZeroPrice`, `rejectsNegativePrice` | `multiplePassengers` |
| `BookingService.cancel`: id bosh | `rejectsBlankBookingId` | `cancelMarksBookingAsCancelled` |
| `BookingService.cancel`: rezervimi nuk ekziston | `rejectsUnknownBooking` | `cancelRestoresSeats` |
| `Booking.cancel`: tashmë i anuluar | `rejectsDoubleCancellation` | `cancelMarksBookingAsCancelled` |
| `RouteSearchService.search`: origjina = destinacioni | `rejectsSameOriginAndDestination` | `findsMatchingTrips` |
