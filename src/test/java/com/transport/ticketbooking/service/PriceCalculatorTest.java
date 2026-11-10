package com.transport.ticketbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Feature 3 - Ticket price calculation. Tests written BEFORE the implementation (TDD). */
class PriceCalculatorTest {

    private PriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new PriceCalculator();
    }

    // ---------- normal cases ----------

    @Test
    @DisplayName("price for one passenger equals the unit price")
    void singlePassenger() {
        assertEquals(new BigDecimal("10.00"), calculator.calculateTotal(new BigDecimal("10.00"), 1));
    }

    @Test
    @DisplayName("total is unit price multiplied by the number of passengers")
    void multiplePassengers() {
        assertEquals(new BigDecimal("37.50"), calculator.calculateTotal(new BigDecimal("12.50"), 3));
    }

    @Test
    @DisplayName("total is always expressed with two decimal places")
    void resultHasTwoDecimalPlaces() {
        assertEquals(new BigDecimal("20.00"), calculator.calculateTotal(new BigDecimal("10"), 2));
    }

    // ---------- boundary cases ----------

    @Test
    @DisplayName("supports the maximum of 10 passengers")
    void maximumPassengers() {
        assertEquals(new BigDecimal("72.50"), calculator.calculateTotal(new BigDecimal("7.25"), 10));
    }

    @Test
    @DisplayName("supports the smallest positive price of 0.01")
    void smallestPositivePrice() {
        assertEquals(new BigDecimal("0.01"), calculator.calculateTotal(new BigDecimal("0.01"), 1));
    }

    @Test
    @DisplayName("rounds the total half-up to two decimals (9.999 -> 10.00)")
    void roundsUpAtThirdDecimal() {
        assertEquals(new BigDecimal("10.00"), calculator.calculateTotal(new BigDecimal("3.333"), 3));
    }

    @Test
    @DisplayName("rounds exactly half up (0.005 -> 0.01)")
    void roundsExactHalfUp() {
        assertEquals(new BigDecimal("0.01"), calculator.calculateTotal(new BigDecimal("0.005"), 1));
    }

    // ---------- invalid input ----------

    @Test
    @DisplayName("rejects a null unit price")
    void rejectsNullPrice() {
        assertThrows(IllegalArgumentException.class, () -> calculator.calculateTotal(null, 1));
    }

    @Test
    @DisplayName("rejects a negative unit price")
    void rejectsNegativePrice() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateTotal(new BigDecimal("-0.01"), 1));
    }

    @Test
    @DisplayName("rejects a unit price of zero")
    void rejectsZeroPrice() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateTotal(BigDecimal.ZERO, 1));
    }

    @Test
    @DisplayName("rejects zero passengers")
    void rejectsZeroPassengers() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateTotal(new BigDecimal("10.00"), 0));
    }

    @Test
    @DisplayName("rejects a negative number of passengers")
    void rejectsNegativePassengers() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateTotal(new BigDecimal("10.00"), -3));
    }

    @Test
    @DisplayName("rejects more than 10 passengers")
    void rejectsTooManyPassengers() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.calculateTotal(new BigDecimal("10.00"), 11));
    }
}
