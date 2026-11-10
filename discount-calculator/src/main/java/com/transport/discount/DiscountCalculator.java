package com.transport.discount;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountCalculator {

    public BigDecimal calculateDiscount(BigDecimal amount, CustomerType customerType) {
        if (amount == null || customerType == null) {
            throw new IllegalArgumentException("amount and customer type are required");
        }
        if (amount.signum() <= 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        BigDecimal rate;
        if (customerType == CustomerType.PREMIUM) {
            if (amount.compareTo(new BigDecimal("100")) >= 0) {
                rate = new BigDecimal("0.20");
            } else {
                rate = new BigDecimal("0.10");
            }
        } else {
            if (amount.compareTo(new BigDecimal("200")) >= 0) {
                rate = new BigDecimal("0.05");
            } else {
                rate = BigDecimal.ZERO;
            }
        }
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
