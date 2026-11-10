package com.transport.ticketbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transport.ticketbooking.exception.NoSeatsAvailableException;
import com.transport.ticketbooking.model.Booking;
import com.transport.ticketbooking.model.Trip;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Feature 2 - Ticket booking. Tests written BEFORE the implementation (TDD). */
class BookingServiceTest {

    private BookingService service;

    private static Trip tripWithSeats(int seats) {
        return new Trip("T1", "Prishtina", "Prizren",
                LocalDateTime.of(2026, 12, 1, 8, 0), seats, new BigDecimal("10.00"));
    }

    @BeforeEach
    void setUp() {
        service = new BookingService();
    }

    // ---------- normal cases ----------

    @Test
    @DisplayName("creates a booking confirmation with trip, passenger and seat count")
    void createsBooking() {
        Trip trip = tripWithSeats(40);

        Booking booking = service.book(trip, "Alban", 2);

        assertNotNull(booking.getId());
        assertSame(trip, booking.getTrip());
        assertEquals("Alban", booking.getPassengerName());
        assertEquals(2, booking.getSeats());
    }

    @Test
    @DisplayName("reduces the available seats of the trip by the booked amount")
    void reducesAvailableSeats() {
        Trip trip = tripWithSeats(40);

        service.book(trip, "Alban", 2);

        assertEquals(38, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("gives every booking a different, sequential id")
    void bookingIdsAreUnique() {
        Trip trip = tripWithSeats(40);

        Booking first = service.book(trip, "Alban", 1);
        Booking second = service.book(trip, "Era", 1);

        assertEquals("BK-1", first.getId());
        assertEquals("BK-2", second.getId());
    }

    // ---------- boundary cases ----------

    @Test
    @DisplayName("accepts the minimum of 1 passenger")
    void acceptsMinimumQuantity() {
        assertEquals(1, service.book(tripWithSeats(5), "Alban", 1).getSeats());
    }

    @Test
    @DisplayName("accepts the maximum of 10 passengers")
    void acceptsMaximumQuantity() {
        assertEquals(10, service.book(tripWithSeats(40), "Alban", 10).getSeats());
    }

    @Test
    @DisplayName("accepts a booking for exactly the remaining seats")
    void bookingExactlyRemainingSeats() {
        Trip trip = tripWithSeats(3);

        service.book(trip, "Alban", 3);

        assertEquals(0, trip.getAvailableSeats());
    }

    // ---------- no seats available ----------

    @Test
    @DisplayName("rejects a booking when the trip is sold out")
    void rejectsWhenSoldOut() {
        assertThrows(NoSeatsAvailableException.class, () -> service.book(tripWithSeats(0), "Alban", 1));
    }

    @Test
    @DisplayName("rejects a booking for one seat more than available and leaves seats unchanged")
    void rejectsOneSeatTooMany() {
        Trip trip = tripWithSeats(3);

        assertThrows(NoSeatsAvailableException.class, () -> service.book(trip, "Alban", 4));

        assertEquals(3, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("prevents overbooking: a second booking fails after the trip becomes full")
    void preventsOverbooking() {
        Trip trip = tripWithSeats(2);
        service.book(trip, "Alban", 2);

        assertThrows(NoSeatsAvailableException.class, () -> service.book(trip, "Era", 1));
    }

    // ---------- invalid input ----------

    @Test
    @DisplayName("rejects zero passengers")
    void rejectsZeroPassengers() {
        assertThrows(IllegalArgumentException.class, () -> service.book(tripWithSeats(40), "Alban", 0));
    }

    @Test
    @DisplayName("rejects a negative number of passengers")
    void rejectsNegativePassengers() {
        assertThrows(IllegalArgumentException.class, () -> service.book(tripWithSeats(40), "Alban", -1));
    }

    @Test
    @DisplayName("rejects more than 10 passengers in one booking and does not touch the seats")
    void rejectsMoreThanMaximum() {
        Trip trip = tripWithSeats(40);

        assertThrows(IllegalArgumentException.class, () -> service.book(trip, "Alban", 11));

        assertEquals(40, trip.getAvailableSeats());
    }

    @Test
    @DisplayName("rejects a missing trip")
    void rejectsNullTrip() {
        assertThrows(IllegalArgumentException.class, () -> service.book(null, "Alban", 1));
    }

    @Test
    @DisplayName("rejects a null passenger name")
    void rejectsNullPassengerName() {
        assertThrows(IllegalArgumentException.class, () -> service.book(tripWithSeats(40), null, 1));
    }

    @Test
    @DisplayName("rejects a blank passenger name")
    void rejectsBlankPassengerName() {
        assertThrows(IllegalArgumentException.class, () -> service.book(tripWithSeats(40), "  ", 1));
    }
}
