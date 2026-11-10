# DiscountCalculator — TDD exercise

Small exercise inside the *Public Transport Ticket Booking* project, developed test-first.
It lives in its own folder with its own `pom.xml`, so it does not affect the main project.

## Rules (`calculateDiscount(amount, customerType)`)

1. `amount <= 0` → discount `0`
2. Premium and `amount >= 100` → 20%
3. Premium and `amount < 100` → 10%
4. Not premium and `amount >= 200` → 5%
5. Otherwise → no discount

The method returns the **discount amount** (`BigDecimal`, 2 decimals, half-up rounding).
A `null` amount or `null` customer type throws `IllegalArgumentException`.

## Run the tests

```bash
cd discount-calculator
mvn test
```

## TDD steps (see `git log`)

1. **RED** — 14 tests written first (all rules, boundaries 100 / 99.99 / 200 / 199.99, invalid input); they fail against a not-implemented skeleton.
2. **GREEN** — minimal implementation; all tests pass.
3. **REFACTOR** — named constants and small helper methods; behaviour unchanged, tests still green.
