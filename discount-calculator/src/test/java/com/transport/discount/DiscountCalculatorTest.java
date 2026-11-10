package com.transport.discount;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for DiscountCalculator, written BEFORE the implementation (TDD).
 *
 * Rules under test:
 *  1. amount <= 0                       -> discount 0
 *  2. Premium and amount >= 100         -> 20%
 *  3. Premium and amount < 100          -> 10%
 *  4. Not premium and amount >= 200     -> 5%
 *  5. otherwise                         -> no discount
 */
class DiscountCalculatorTest {

    private DiscountCalculator calculator;

    private static BigDecimal amount(String value) {
        return new BigDecimal(value);
    }

    private BigDecimal discount(String value, CustomerType type) {
        return calculator.calculateDiscount(amount(value), type);
    }

    @BeforeEach
    void setUp() {
        calculator = new DiscountCalculator();
    }

    // ---------- Rule 1: amount <= 0 ----------

    @Test
    @DisplayName("rule 1: zero amount gives no discount")
    void zeroAmountGivesNoDiscount() {
        assertEquals(amount("0.00"), discount("0", CustomerType.STANDARD));
    }

    @Test
    @DisplayName("rule 1: negative amount gives no discount")
    void negativeAmountGivesNoDiscount() {
        assertEquals(amount("0.00"), discount("-50", CustomerType.STANDARD));
    }

    @Test
    @DisplayName("rule 1 has priority: premium customer with zero amount gets no discount")
    void premiumWithZeroAmountGivesNoDiscount() {
        assertEquals(amount("0.00"), discount("0", CustomerType.PREMIUM));
    }

    @Test
    @DisplayName("rule 1 has priority: premium customer with negative amount gets no discount")
    void premiumWithNegativeAmountGivesNoDiscount() {
        assertEquals(amount("0.00"), discount("-1", CustomerType.PREMIUM));
    }

    // ---------- Rule 2: premium, amount >= 100 -> 20% ----------

    @Test
    @DisplayName("rule 2 boundary: premium with exactly 100 gets 20%")
    void premiumAtHundredGetsTwentyPercent() {
        assertEquals(amount("20.00"), discount("100", CustomerType.PREMIUM));
    }

    @Test
    @DisplayName("rule 2: premium with 250 gets 20%")
    void premiumAboveHundredGetsTwentyPercent() {
        assertEquals(amount("50.00"), discount("250", CustomerType.PREMIUM));
    }

    // ---------- Rule 3: premium, amount < 100 -> 10% ----------

    @Test
    @DisplayName("rule 3 boundary: premium with 99.99 gets 10% (rounded to 10.00)")
    void premiumJustBelowHundredGetsTenPercent() {
        assertEquals(amount("10.00"), discount("99.99", CustomerType.PREMIUM));
    }

    @Test
    @DisplayName("rule 3: premium with 50 gets 10%")
    void premiumBelowHundredGetsTenPercent() {
        assertEquals(amount("5.00"), discount("50", CustomerType.PREMIUM));
    }

    // ---------- Rule 4: not premium, amount >= 200 -> 5% ----------

    @Test
    @DisplayName("rule 4 boundary: standard customer with exactly 200 gets 5%")
    void standardAtTwoHundredGetsFivePercent() {
        assertEquals(amount("10.00"), discount("200", CustomerType.STANDARD));
    }

    @Test
    @DisplayName("rule 4: standard customer with 500 gets 5%")
    void standardAboveTwoHundredGetsFivePercent() {
        assertEquals(amount("25.00"), discount("500", CustomerType.STANDARD));
    }

    // ---------- Rule 5: otherwise no discount ----------

    @Test
    @DisplayName("rule 5 boundary: standard customer with 199.99 gets no discount")
    void standardJustBelowTwoHundredGetsNoDiscount() {
        assertEquals(amount("0.00"), discount("199.99", CustomerType.STANDARD));
    }

    @Test
    @DisplayName("rule 5: the premium threshold of 100 does not apply to standard customers")
    void standardWithHundredGetsNoDiscount() {
        assertEquals(amount("0.00"), discount("100", CustomerType.STANDARD));
    }

    // ---------- invalid input ----------

    @Test
    @DisplayName("rejects a null amount")
    void rejectsNullAmount() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscount(null, CustomerType.PREMIUM));
    }

    @Test
    @DisplayName("rejects a null customer type")
    void rejectsNullCustomerType() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateDiscount(amount("100"), null));
    }
}
