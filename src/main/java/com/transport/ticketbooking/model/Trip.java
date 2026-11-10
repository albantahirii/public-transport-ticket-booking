package com.transport.ticketbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trip {

    public Trip(String id, String origin, String destination, LocalDateTime departure,
                int availableSeats, BigDecimal unitPrice) {
        throw new UnsupportedOperationException("not implemented");
    }

    public String getId() {
        throw new UnsupportedOperationException("not implemented");
    }
}
