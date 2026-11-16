package com.transport.ticketbooking.service;

import com.transport.ticketbooking.model.Booking;
import com.transport.ticketbooking.model.Trip;
import java.util.HashMap;
import java.util.Map;

/** Creates and cancels bookings; prevents overbooking. */
public class BookingService {

    private final Map<String, Booking> bookings = new HashMap<>();
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
        Booking booking = new Booking("BK-" + lastBookingNumber, trip, passengerName, seats);
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public void cancel(String bookingId) {
        if (bookingId == null || bookingId.isBlank()) {
            throw new IllegalArgumentException("booking id is required");
        }
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("booking " + bookingId + " not found");
        }
        booking.cancel();
        booking.getTrip().releaseSeats(booking.getSeats());
    }
}
