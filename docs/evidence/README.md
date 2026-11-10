# Test execution evidence — how these files were produced

Each `.txt` file here is the **unedited console output of a real test run** captured at one stage of the TDD cycle (see `../TDD_LOG.md`).

## Important: which runner produced them

These runs were produced in an offline authoring sandbox where **Maven Central was blocked**, so `mvn test` and the JUnit 5 (`junit-jupiter`) libraries could not be downloaded. To still execute the tests honestly, the test classes were compiled with `javac` against a tiny throw-away stand-in for the handful of JUnit annotations/assertions used (`@Test`, `@DisplayName`, `@BeforeEach`, `assertEquals`, `assertTrue`, `assertFalse`, `assertThrows`, `assertNotNull`, `assertSame`) and run with a small reflection-based runner.

- The test sources in `src/test/java` are written against the **real JUnit 5 API** and are unchanged.
- The result lines `[PASS]`/`[FAIL]` and "Tests run: N, Failures: M" come from that stand-in runner, **not** from Maven Surefire.
- Behaviour of the production code (what passes/fails) is real; the report format is not Surefire's.

## Reproduce with real Maven + JUnit 5 (recommended before submitting)

```bash
mvn test
```

Expected result at the final commit: `Tests run: 42, Failures: 0, Errors: 0, Skipped: 0`. Replace or add a screenshot of that run in the submission report.

## File index

| File | Stage | Observed result |
|---|---|---|
| `f1-1-red-no-production-code.txt` | Route search — RED (tests only) | Test compilation fails: production classes do not exist |
| `f1-2-red-stub-fails.txt` | Route search — RED (stubs) | 14 run, 14 failed |
| `f1-3-green.txt` | Route search — GREEN | 14 run, 0 failed |
| `f1-4-refactor-green.txt` | Route search — REFACTOR | 14 run, 0 failed |
| `f2-1-red-no-production-code.txt` | Booking — RED (tests only) | Test compilation fails |
| `f2-2-red-stub-fails.txt` | Booking — RED (stubs) | 15 failed of 15 new tests (29 run in total, 14 old tests still pass) |
| `f2-3-green.txt` | Booking — GREEN | 29 run, 0 failed |
| `f3-1-red-no-production-code.txt` | Price — RED (tests only) | Test compilation fails |
| `f3-2-red-stub-fails.txt` | Price — RED (stub) | 13 failed of 13 new tests (42 run in total) |
| `f3-3-green.txt` | Price — GREEN | 42 run, 0 failed |
| `f3-4-refactor-green.txt` | Shared refactoring | 42 run, 0 failed |
