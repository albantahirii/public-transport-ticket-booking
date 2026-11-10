package com.transport.ticketbooking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculator {

    public BigDecimal calculateTotal(BigDecimal unitPrice, int passengers) {
        if (unitPrice == null || unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("unit price must be positive");
        }
        if (passengers < 1 || passengers > 10) {
            throw new IllegalArgumentException("passengers must be between 1 and 10");
        }
        return unitPrice.multiply(BigDecimal.valueOf(passengers)).setScale(2, RoundingMode.HALF_UP);
    }
}
