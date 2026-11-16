package com.transport.ticketbooking.model;

import com.transport.ticketbooking.exception.NoSeatsAvailableException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Trip {

    private final String id;
    private final String origin;
    private final String destination;
    private final LocalDateTime departure;
    private int availableSeats;
    private final BigDecimal unitPrice;

    public Trip(String id, String origin, String destination, LocalDateTime departure,
                int availableSeats, BigDecimal unitPrice) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.availableSeats = availableSeats;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDeparture() {
        return departure;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void reserveSeats(int seats) {
        if (seats > availableSeats) {
            throw new NoSeatsAvailableException(
                    "Requested " + seats + " seat(s) but only " + availableSeats + " available");
        }
        availableSeats -= seats;
    }

    public void releaseSeats(int seats) {
        availableSeats += seats;
    }
}
