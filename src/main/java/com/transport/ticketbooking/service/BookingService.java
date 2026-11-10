package com.transport.ticketbooking.service;

import com.transport.ticketbooking.model.Booking;
import com.transport.ticketbooking.model.Trip;

/** Creates bookings and prevents overbooking. */
public class BookingService {

    private int lastBookingNumber = 0;

    public Booking book(Trip trip, String passengerName, int seats) {
        if (trip == null) {
            throw new IllegalArgumentException("trip is required");
        }
        if (passengerName == null || passengerName.isBlank()) {
            throw new IllegalArgumentException("passenger name is required");
        }
        PassengerCount.require(seats);
        trip.reserveSeats(seats);
        lastBookingNumber++;
        return new Booking("BK-" + lastBookingNumber, trip, passengerName, seats);
    }
}
