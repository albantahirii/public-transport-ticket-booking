package com.transport.ticketbooking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** Calculates the total ticket price: unit price x passengers, rounded half-up to 2 decimals. */
public class PriceCalculator {

    public BigDecimal calculateTotal(BigDecimal unitPrice, int passengers) {
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("unit price must be positive");
        }
        PassengerCount.require(passengers);
        return unitPrice.multiply(BigDecimal.valueOf(passengers)).setScale(2, RoundingMode.HALF_UP);
    }
}
