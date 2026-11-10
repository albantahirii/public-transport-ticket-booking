package com.transport.ticketbooking.service;

import com.transport.ticketbooking.model.Booking;
import com.transport.ticketbooking.model.Trip;

public class BookingService {

    private int lastBookingNumber = 0;

    public Booking book(Trip trip, String passengerName, int seats) {
        if (trip == null) {
            throw new IllegalArgumentException("trip is required");
        }
        if (passengerName == null || passengerName.isBlank()) {
            throw new IllegalArgumentException("passenger name is required");
        }
        if (seats < 1 || seats > 10) {
            throw new IllegalArgumentException("seats must be between 1 and 10");
        }
        trip.reserveSeats(seats);
        lastBookingNumber++;
        return new Booking("BK-" + lastBookingNumber, trip, passengerName, seats);
    }
}
