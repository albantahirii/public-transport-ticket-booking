package com.transport.ticketbooking.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transport.ticketbooking.model.Trip;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Feature 1 - Route search. Tests written BEFORE the implementation (TDD). */
class RouteSearchServiceTest {

    private static final LocalDate DAY = LocalDate.of(2026, 12, 1);

    private RouteSearchService service;

    private static Trip trip(String id, String from, String to, LocalDateTime departure) {
        return new Trip(id, from, to, departure, 40, new BigDecimal("10.00"));
    }

    @BeforeEach
    void setUp() {
        service = new RouteSearchService(List.of(
                trip("T1", "Prishtina", "Prizren", DAY.atTime(8, 0)),
                trip("T2", "Prishtina", "Prizren", DAY.atTime(14, 30)),
                trip("T3", "Prishtina", "Peja", DAY.atTime(9, 0)),
                trip("T4", "Prishtina", "Prizren", DAY.plusDays(1).atTime(8, 0))));
    }

    // ---------- normal cases ----------

    @Test
    @DisplayName("finds all trips matching origin, destination and date")
    void findsMatchingTrips() {
        List<Trip> result = service.search("Prishtina", "Prizren", DAY);

        assertEquals(2, result.size());
        assertEquals("T1", result.get(0).getId());
        assertEquals("T2", result.get(1).getId());
    }

    @Test
    @DisplayName("does not return trips that run on a different date")
    void excludesOtherDates() {
        List<Trip> result = service.search("Prishtina", "Prizren", DAY.plusDays(1));

        assertEquals(1, result.size());
        assertEquals("T4", result.get(0).getId());
    }

    @Test
    @DisplayName("does not match the reverse direction of a route")
    void reverseDirectionNotMatched() {
        assertTrue(service.search("Prizren", "Prishtina", DAY).isEmpty());
    }

    // ---------- route does not exist ----------

    @Test
    @DisplayName("returns an empty list when the route does not exist")
    void unknownRouteReturnsEmptyList() {
        assertTrue(service.search("Prishtina", "Gjakova", DAY).isEmpty());
    }

    @Test
    @DisplayName("returns an empty list when no trip runs on the requested date")
    void noTripsOnDateReturnsEmptyList() {
        assertTrue(service.search("Prishtina", "Prizren", DAY.plusDays(30)).isEmpty());
    }

    // ---------- boundary cases ----------

    @Test
    @DisplayName("matching ignores letter case and surrounding whitespace")
    void matchingIgnoresCaseAndWhitespace() {
        assertEquals(2, service.search("  prishtina ", "PRIZREN", DAY).size());
    }

    @Test
    @DisplayName("includes trips at 00:00 and 23:59 of the date but not 00:00 of the next day")
    void dayBoundaries() {
        RouteSearchService edge = new RouteSearchService(List.of(
                trip("A", "X", "Y", DAY.atStartOfDay()),
                trip("B", "X", "Y", DAY.atTime(23, 59)),
                trip("C", "X", "Y", DAY.plusDays(1).atStartOfDay())));

        List<Trip> result = edge.search("X", "Y", DAY);

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getId());
        assertEquals("B", result.get(1).getId());
    }

    @Test
    @DisplayName("works with an empty timetable")
    void emptyTimetable() {
        assertTrue(new RouteSearchService(List.of()).search("A", "B", DAY).isEmpty());
    }

    // ---------- invalid input ----------

    @Test
    @DisplayName("rejects a null origin")
    void rejectsNullOrigin() {
        assertThrows(IllegalArgumentException.class, () -> service.search(null, "Prizren", DAY));
    }

    @Test
    @DisplayName("rejects a blank origin")
    void rejectsBlankOrigin() {
        assertThrows(IllegalArgumentException.class, () -> service.search("   ", "Prizren", DAY));
    }

    @Test
    @DisplayName("rejects a null destination")
    void rejectsNullDestination() {
        assertThrows(IllegalArgumentException.class, () -> service.search("Prishtina", null, DAY));
    }

    @Test
    @DisplayName("rejects a blank destination")
    void rejectsBlankDestination() {
        assertThrows(IllegalArgumentException.class, () -> service.search("Prishtina", "", DAY));
    }

    @Test
    @DisplayName("rejects a null travel date")
    void rejectsNullDate() {
        assertThrows(IllegalArgumentException.class, () -> service.search("Prishtina", "Prizren", null));
    }

    @Test
    @DisplayName("rejects identical origin and destination")
    void rejectsSameOriginAndDestination() {
        assertThrows(IllegalArgumentException.class, () -> service.search("Prishtina", "prishtina", DAY));
    }
}
