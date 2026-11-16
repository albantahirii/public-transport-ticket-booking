package com.transport.ticketbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.transport.ticketbooking.model.Booking;
import com.transport.ticketbooking.model.Trip;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Feature 4 - Booking cancellation. Tests written BEFORE the implementation (TDD). */
class BookingCancellationTest {

    private BookingService service;
    private Trip trip;

    @BeforeEach
    void setUp() {
        service = new BookingService();
        trip = new Trip("T1", "Prishtina", "Prizren",
                LocalDateTime.of(2026, 12, 1, 8, 0), 10, new BigDecimal("10.00"));
    }

    // ---------- positive cases ----------

    @Test
    @DisplayName("cancelling a booking marks it as cancelled")
    void cancelMarksBookingAsCancelled() {
        Booking booking = service.book(trip, "Alban", 2);

        service.cancel(booking.getId());

        assertTrue(booking.isCancelled());
    }

    @Test
    @DisplayName("cancelling a booking gives its seats back to the trip")
    void cancelRestoresSeats() {
        Booking booking = service.book(trip, "Alban", 3);

        service.cancel(booking.getId());

        assertEquals(10, trip.getAvailableSeats());
    }

    // ---------- negative cases ----------

    @Test
    @DisplayName("rejects cancelling a booking that does not exist")
    void rejectsUnknownBooking() {
        assertThrows(IllegalArgumentException.class, () -> service.cancel("BK-99"));
    }

    @Test
    @DisplayName("rejects a blank booking id")
    void rejectsBlankBookingId() {
        assertThrows(IllegalArgumentException.class, () -> service.cancel(" "));
    }

    @Test
    @DisplayName("rejects cancelling the same booking twice and does not give seats back twice")
    void rejectsDoubleCancellation() {
        Booking booking = service.book(trip, "Alban", 2);
        service.cancel(booking.getId());

        assertThrows(IllegalStateException.class, () -> service.cancel(booking.getId()));
        assertEquals(10, trip.getAvailableSeats());
    }
}
