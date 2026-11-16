# Public Transport Ticket Booking System

Aplikacion Java për rezervimin e biletave të transportit publik, i zhvilluar me **Test-Driven Development (TDD)**.

> **Lënda:** Test Driven Development — Inxhinieri Softuerike, semestri 3 (Master)
> **Faza aktuale:** Java 3 — funksionalitetet kryesore dhe testet automatike

## Përshkrimi

Pasagjerët duhet të gjejnë udhëtimet e lira, të rezervojnë ulëse dhe të dinë sa paguajnë; operatori duhet të shmangë mbirezervimin. Aplikacioni zbaton këtë logjikë biznesi, të mbuluar plotësisht me teste.

## Funksionalitetet

| # | Funksionaliteti | Klasa | Rregullat kryesore |
|---|---|---|---|
| 1 | Kërkimi i udhëtimeve | `RouteSearchService` | sipas origjinës, destinacionit dhe datës; pa dallim shkronjash të mëdha/vogla |
| 2 | Rezervimi i ulëseve | `BookingService.book` | 1–10 pasagjerë; nuk lejohet mbirezervimi |
| 3 | Llogaritja e çmimit | `PriceCalculator` | çmimi × pasagjerët, 2 decimale (half-up) |
| 4 | Anulimi i rezervimit | `BookingService.cancel` | ulëset kthehen; anulimi i dyfishtë refuzohet |

## Arkitektura

Arkitekturë me shtresa: logjika e biznesit është te `service`, të dhënat te `model`, gabimet e domenit te `exception`.
cat > README.md <<'EOF'
# Public Transport Ticket Booking System

Aplikacion Java për rezervimin e biletave të transportit publik, i zhvilluar me **Test-Driven Development (TDD)**.

> **Lënda:** Test Driven Development — Inxhinieri Softuerike, semestri 3 (Master)
> **Faza aktuale:** Java 3 — funksionalitetet kryesore dhe testet automatike

## Përshkrimi

Pasagjerët duhet të gjejnë udhëtimet e lira, të rezervojnë ulëse dhe të dinë sa paguajnë; operatori duhet të shmangë mbirezervimin. Aplikacioni zbaton këtë logjikë biznesi, të mbuluar plotësisht me teste.

## Funksionalitetet

| # | Funksionaliteti | Klasa | Rregullat kryesore |
|---|---|---|---|
| 1 | Kërkimi i udhëtimeve | `RouteSearchService` | sipas origjinës, destinacionit dhe datës; pa dallim shkronjash të mëdha/vogla |
| 2 | Rezervimi i ulëseve | `BookingService.book` | 1–10 pasagjerë; nuk lejohet mbirezervimi |
| 3 | Llogaritja e çmimit | `PriceCalculator` | çmimi × pasagjerët, 2 decimale (half-up) |
| 4 | Anulimi i rezervimit | `BookingService.cancel` | ulëset kthehen; anulimi i dyfishtë refuzohet |

## Arkitektura

Arkitekturë me shtresa: logjika e biznesit është te `service`, të dhënat te `model`, gabimet e domenit te `exception`.

~~~
src/main/java/com/transport/ticketbooking/
├── model/       Trip, Booking                       (të dhënat dhe rregullat e tyre)
├── service/     RouteSearchService, BookingService,
│                PriceCalculator, PassengerCount     (logjika e biznesit)
└── exception/   NoSeatsAvailableException           (gabimet e domenit)

src/test/java/com/transport/ticketbooking/service/
├── RouteSearchServiceTest
├── BookingServiceTest
├── PriceCalculatorTest
└── BookingCancellationTest
~~~

Vendime teknike:

- **`BigDecimal`** për çmimet, jo `double`, që të mos ketë gabime rrumbullakimi me para.
- **`PassengerCount`**: rregulli "1–10 pasagjerë" është në një vend të vetëm dhe përdoret nga rezervimi e çmimi.
- **`IllegalArgumentException`** për input të gabuar, **`IllegalStateException`** për veprim të palejuar (p.sh. anulim i dyfishtë), **`NoSeatsAvailableException`** kur s'ka ulëse.
- Të dhënat ruhen në memorie; databaza (H2) planifikohet për javët në vijim.

## Teknologjitë

| Qëllimi | Teknologjia |
|---|---|
| Gjuha | Java 17+ |
| Build | Maven |
| Testimi | JUnit 5 (`junit-jupiter`, përfshirë `@ParameterizedTest`) |
| Versionimi | Git + GitHub |

## Konfigurimi dhe ekzekutimi

Kërkesat: **JDK 17+** dhe **Maven 3.8+**.

~~~bash
git clone https://github.com/albantahirii/public-transport-ticket-booking.git
cd public-transport-ticket-booking
mvn clean test
~~~

Rezultati i pritur: `Tests run: 49, Failures: 0, Errors: 0` dhe `BUILD SUCCESS`.

## Testet

| Lloji | Shembull |
|---|---|
| Pozitive / negative | rezervim i vlefshëm ✔ / emër bosh, 0 pasagjerë, anulim i dyfishtë ✘ |
| Vlerat kufitare | pasagjerët 0, 1, 2, 9, 10, 11; ulëset "saktë sa ka" dhe "+1"; ora 00:00 dhe 23:59 |
| Branch testing | çdo `if` testohet në të dyja degët (tabela te `docs/TDD_LOG.md`) |
| Parameterized | `PriceCalculatorTest`: `@ValueSource` dhe `@CsvSource` |

## Procesi TDD

1. **RED** — shkruhet testi i pari dhe dështon.
2. **GREEN** — shkruhet kodi minimal që testi të kalojë.
3. **REFACTOR** — përmirësohet kodi, testet mbeten të gjelbra.

Çdo fazë ka commit-in e vet (`test(...): RED`, `feat(...): GREEN`, `refactor(...)`), prandaj cikli duket në `git log`. Detajet: [`docs/TDD_LOG.md`](docs/TDD_LOG.md).

## Dokumentet

- `docs/PROJECT_PROPOSAL_SQ.md` — propozimi i projektit
- `docs/REQUIREMENTS_ARCHITECTURE.md` — kërkesat dhe arkitektura
- `docs/TDD_LOG.md` — ditari i cikleve RED–GREEN–REFACTOR
- `discount-calculator/` — ushtrim i veçantë TDD me `pom.xml` të vetin (nuk është pjesë e build-it kryesor)

## Autori

Alban Tahiri ([@albantahirii](https://github.com/albantahirii))
