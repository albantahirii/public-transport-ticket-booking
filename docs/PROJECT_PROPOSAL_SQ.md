# Projekt-propozim — Sistemi i Rezervimit të Biletave të Transportit Publik

**Lënda:** Zhvillimi i drejtuar nga testimi (Test Driven Development – TDD) — Master në Inxhinieri Softuerike, Semestri 3
**Dorëzimi:** Java 1 (afati: 9 nëntor 2026)

## 1. Titulli

Sistemi i Rezervimit të Biletave të Transportit Publik, i zhvilluar me Test Driven Development.

## 2. Problemi që synon të zgjidhë

Pasagjerët që duan të udhëtojnë me autobus duhet të dinë cilat udhëtime ekzistojnë mes dy vendeve në një datë të caktuar, nëse ka ende vende të lira dhe sa kushton udhëtimi për grupin e tyre. Nga ana e operatorit, sistemi i rezervimit nuk duhet të shesë kurrë më shumë vende sa ka mjeti (overbooking) dhe duhet t'i llogarisë çmimet në mënyrë konsistente. Gabimet në këto rregulla sjellin humbje financiare dhe pakënaqësi te pasagjerët. Kjo e bën domenin shumë të përshtatshëm për qasjen "testi së pari": rregullat janë të sakta, kanë kufij të qartë (numri i vendeve, limiti i pasagjerëve, rrumbullakimi) dhe shprehen lehtë si teste automatike.

## 3. Qëllimi dhe objektivat

Qëllimi është ndërtimi i një back-end-i të vogël por të mirëinxhinieruar për rezervime, **dhe demonstrimi i një procesi sistematik TDD gjatë ndërtimit**. Objektivat:

1. Implementimi i rregullave kryesore (kërkimi, rezervimi, disponueshmëria e vendeve, llogaritja e çmimit, menaxhimi i rezervimeve) brenda një shtrese shërbimesh të pastër dhe të testueshëm.
2. Aplikimi i ciklit Red–Green–Refactor për çdo sjellje dhe ruajtja e historikut në Git si dëshmi.
3. Testimi i rasteve normale, kufitare dhe me input të pavlefshëm për çdo funksionalitet.
4. Ndjekja e fazave të lëndës: unit teste (Java 2), Mockito dhe teste për përjashtime (Java 3), REST API dhe teste integrimi/databaze (Java 4), analiza e mbulimit me JaCoCo (Java 5), demonstrimi përfundimtar (Java 6).

## 4. Përdoruesit e synuar

- **Pasagjerët**, që kërkojnë udhëtime, rezervojnë bileta dhe më vonë shohin ose anulojnë rezervimet.
- **Operatorët e transportit / stafi i biletarisë**, që kanë nevojë për llogaritje të besueshme të vendeve dhe tarifave.
- **Vlerësuesit e lëndës**, që kanë nevojë për një regjistër të qartë dhe të riprodhueshëm se si u zhvillua dhe u testua softueri.

## 5. Funksionalitetet kryesore

| # | Funksionaliteti | Përshkrim i shkurtër | Java 1 |
|---|---|---|---|
| 1 | Kërkimi i rrugëve dhe udhëtimeve | Gjen udhëtimet sipas vendit të nisjes, destinacionit dhe datës | **Testuar dhe implementuar** |
| 2 | Rezervimi i biletave | Rezervon vende në një udhëtim dhe jep konfirmim rezervimi | **Testuar dhe implementuar** |
| 3 | Llogaritja e çmimit të biletës | Çmimi total = çmimi për bilete × pasagjerë, rrumbullakuar në dy decimale | **Testuar dhe implementuar** |
| 4 | Disponueshmëria e vendeve | Kontrollon vendet e lira dhe parandalon overbooking | Rregulli bazë është te rezervimi; zgjerohet më vonë |
| 5 | Menaxhimi i rezervimeve | Shikimi dhe anulimi i rezervimeve ekzistuese | Planifikuar (Java 2+) |

## 6. Teknologjitë

Java 17+, Maven, JUnit 5 (unit teste), Mockito (Java 3), Spring Boot REST API dhe databaza H2 (Java 4), JaCoCo (Java 5), Git dhe GitHub.

## 7. Qasja e zhvillimit dhe plani

Çdo sjellje specifikohet fillimisht me një test JUnit që dështon (**Red**), pastaj implementohet me kodin minimal që e kalon testin (**Green**), dhe në fund strukturohet më mirë ndërsa të gjitha testet mbeten të gjelbra (**Refactor**). Arkitektura është me shtresa (model / service / exception, me API dhe persistencë më vonë) dhe varet nga abstraksione aty ku fazat e mëvonshme kërkojnë zëvendësim komponentësh, gjë që e mban kodin të thjeshtë për t'u testuar.

| Java | Fokusi |
|---|---|
| 1 | Kërkesat, repository-ja, README, propozimi, testet dhe implementimi i parë i tri funksionaliteteve |
| 2 | Funksionaliteti bazë me unit teste, trajtimi i inputeve të pavlefshme |
| 3 | Shtresa e shërbimeve, Mockito, trajtimi i përjashtimeve |
| 4 | REST endpoints, teste integrimi dhe databaze |
| 5 | Mbulimi me JaCoCo, refaktorizim, dokumentacioni final |
| 6 | Demonstrimi përfundimtar dhe ekzekutimi live i testeve |

## 8. Rezultati i pritur

Një back-end i funksionueshëm dhe mirë i testuar për rezervim biletash, një repository me historik që tregon ciklet Red–Green–Refactor, dhe një raport përfundimtar testimi e mbulimi që shpjegon vendimet e marra.
