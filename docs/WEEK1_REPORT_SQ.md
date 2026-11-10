# Raporti i Javës së Parë

# Sistemi i Rezervimit të Biletave të Transportit Publik

**Lënda:** Test Driven Development (TDD) — Inxhinieri Softuerike, Semestri 3
**Studenti:** Alban Tahiri
**GitHub repository:** https://github.com/albantahirii/public-transport-ticket-booking

## 1. Përshkrimi i projektit

Projekti është një back-end në Java për rezervimin e biletave të transportit publik (autobusë). Pasagjeri kërkon udhëtime sipas vendit të nisjes, destinacionit dhe datës, rezervon vende në një udhëtim dhe paguan një çmim të llogaritur nga numri i pasagjerëve. Sistemi parandalon overbooking dhe refuzon inputet e pavlefshme. Qëllimi i projektit në lëndë është të tregojë një proces sistematik TDD, prandaj çdo sjellje fillon si një test JUnit 5 që dështon.

Java 1 përfshin kërkesat, përgatitjen e repository-t, propozimin dhe tri funksionalitetet e para të testuara. Ende nuk është ndërtuar REST API, databaza apo ndërfaqe përdoruesi (këto i përkasin javëve të tjera).

## 2. Projekt-propozimi (përmbledhje)

Propozimi i plotë 1–2 faqe gjendet te `docs/PROJECT_PROPOSAL_SQ.md` dhe në Shtojcën A.

- **Problemi:** pasagjerët kanë nevojë për informacion të besueshëm mbi udhëtimet, vendet e lira dhe tarifat; operatorët duhet të shmangin overbooking dhe gabimet në çmim.
- **Objektivat:** implementimi i rregullave kryesore brenda një shtrese shërbimesh të pastër; Red–Green–Refactor për çdo sjellje; testimi i rasteve normale, kufitare dhe të pavlefshme; historik Git që dëshmon procesin.
- **Përdoruesit:** pasagjerët, stafi i biletarisë / operatorët, vlerësuesit e lëndës.
- **Funksionalitetet kryesore:** (1) kërkimi i rrugëve dhe udhëtimeve, (2) rezervimi i biletave, (3) llogaritja e çmimit, (4) disponueshmëria e vendeve / parandalimi i overbooking, (5) menaxhimi i rezervimeve (shikim, anulim — javët e ardhshme).
- **Teknologjitë:** Java 17+, Maven, JUnit 5; më vonë Mockito, Spring Boot, H2, JaCoCo; Git dhe GitHub.

Kërkesat dhe arkitektura janë dokumentuar te `docs/REQUIREMENTS_ARCHITECTURE.md` (dizajn me shtresa: model, service, exception; API dhe persistenca planifikohen për Javën 4).

## 3. Repository

Struktura (shih README.md):

```
pom.xml, README.md, .gitignore
src/main/java/com/transport/ticketbooking/{model,service,exception}
src/test/java/com/transport/ticketbooking/service
docs/  (propozimi, kërkesat/arkitektura, TDD log, raporti, evidence/)
```

Repository-ja (https://github.com/albantahirii/public-transport-ticket-booking) përmban historikun e plotë të commit-eve që regjistrojnë fazat Red–Green–Refactor (shih seksionin 6).

## 4. Tri funksionalitetet e zgjedhura për testim

### 4.1 Kërkimi i rrugëve — `RouteSearchService.search(origin, destination, date)`

Kthen udhëtimet që nisen nga origin drejt destination në datën e dhënë.

- Normale: gjen të gjitha udhëtimet përkatëse; përjashton datat e tjera; drejtimi i kundërt nuk përputhet.
- Rruga nuk ekziston: kthen listë bosh.
- Kufitare: krahasim pa dallim shkronjash dhe hapësirash; udhëtimet në 00:00 dhe 23:59 përfshihen, 00:00 e ditës tjetër jo; orar bosh.
- E pavlefshme: origin/destination null ose bosh, date null, origin e destination identike → `IllegalArgumentException`.

### 4.2 Rezervimi i biletave — `BookingService.book(trip, passengerName, seats)`

Krijon një `Booking` (id `BK-n`) dhe zvogëlon vendet e lira.

- Normale: konfirmimi përmban udhëtimin, pasagjerin dhe numrin e vendeve; vendet zvogëlohen; id-të janë rradhazi.
- Kufitare: 1 pasagjer, 10 pasagjerë, saktësisht vendet e mbetura.
- Pa vende: udhëtim i mbushur, një vend më shumë se sa ka (vendet nuk ndryshojnë), rezervim i dytë kur udhëtimi është plot → `NoSeatsAvailableException`.
- E pavlefshme: 0, −1 ose 11 pasagjerë; trip null; emër pasagjeri null/bosh → `IllegalArgumentException`.

### 4.3 Llogaritja e çmimit — `PriceCalculator.calculateTotal(unitPrice, passengers)`

Totali = çmimi për bilete × pasagjerë, rrumbullakuar half-up në dy decimale (`BigDecimal`).

- Normale: 1 pasagjer (10.00), disa pasagjerë (12.50 × 3 = 37.50), rezultat me dy decimale.
- Kufitare: 10 pasagjerë, çmimi më i vogël 0.01, rrumbullakimi 3.333 × 3 = 9.999 → 10.00 dhe 0.005 → 0.01.
- E pavlefshme: çmim null, negativ ose zero; 0, −3 ose 11 pasagjerë.

Shembull i një testi të shkruar së pari (JUnit 5):

```java
@Test
@DisplayName("rejects a booking for one seat more than available and leaves seats unchanged")
void rejectsOneSeatTooMany() {
    Trip trip = tripWithSeats(3);

    assertThrows(NoSeatsAvailableException.class, () -> service.book(trip, "Alban", 4));

    assertEquals(3, trip.getAvailableSeats());
}
```

## 5. Përmbledhja e testeve

| Klasa e testit | Teste | Normale | Kufitare | Të pavlefshme / dështime |
|---|---|---|---|---|
| `RouteSearchServiceTest` | 14 | 3 | 3 (+2 raste pa rezultat) | 6 |
| `BookingServiceTest` | 15 | 3 | 3 | 3 pa vende/overbooking + 6 të pavlefshme |
| `PriceCalculatorTest` | 13 | 3 | 4 | 6 |
| **Gjithsej** | **42** | | | |

## 6. Dëshmia Red–Green–Refactor dhe rezultatet e ekzekutimit

Regjistri i plotë: `docs/TDD_LOG.md`. Outputet e papërpunuara: `docs/evidence/`.

| Funksionaliteti | RED (vetëm testet) | RED (stub-e që kompilojnë) | GREEN | REFACTOR |
|---|---|---|---|---|
| Kërkimi i rrugëve | nuk kompilohet | 14 të ekzekutuara, 14 dështuan | 14 të ekzekutuara, 0 dështuan | 14 të ekzekutuara, 0 dështuan |
| Rezervimi | nuk kompilohet | 15 nga 15 testet e reja dështuan (29 gjithsej) | 29 të ekzekutuara, 0 dështuan | (me refaktorizimin e përbashkët) |
| Llogaritja e çmimit | nuk kompilohet | 13 nga 13 testet e reja dështuan (42 gjithsej) | 42 të ekzekutuara, 0 dështuan | 42 të ekzekutuara, 0 dështuan |

Rezultati final në commit-in e fundit: **Tests run: 42, Failures: 0** (RouteSearchServiceTest 14, BookingServiceTest 15, PriceCalculatorTest 13).

**Mjedisi i ekzekutimit.** Testet u ekzekutuan me `javac` dhe një runner minimal të pajtueshëm me JUnit, sepse Maven Central nuk ishte i arritshëm në mjedisin e zhvillimit. Kodi i testeve është i pandryshuar dhe synon API-n e JUnit 5; rezultatet pass/fail pasqyrojnë ekzekutim real të kodit, por formati i outputit ndryshon nga Maven Surefire. Ekzekutimi zyrtar është `mvn test` (pritet: Tests run: 42, Failures: 0, Errors: 0, Skipped: 0); shih `docs/evidence/README.md`.

## 7. Përfundim dhe hapat e ardhshëm

Java 1 është përfunduar: projekti është përcaktuar, struktura e repository-t, README dhe propozimi janë përgatitur, dhe tri funksionalitete u zhvilluan me qasjen testi-së-pari me raste normale, kufitare dhe me input të pavlefshëm. Për Javën 2: funksionaliteti bazë (menaxhimi i rezervimeve dhe disponueshmëria e vendeve), më shumë trajtim i inputeve të pavlefshme dhe cikle më të vogla Red–Green–Refactor.
