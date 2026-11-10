package com.transport.ticketbooking.service;

import com.transport.ticketbooking.model.Trip;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/** Finds trips by origin, destination and travel date. */
public class RouteSearchService {

    private final List<Trip> trips;

    public RouteSearchService(List<Trip> trips) {
        this.trips = List.copyOf(trips);
    }

    public List<Trip> search(String origin, String destination, LocalDate date) {
        String from = requireText(origin, "origin");
        String to = requireText(destination, "destination");
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        if (from.equalsIgnoreCase(to)) {
            throw new IllegalArgumentException("origin and destination must differ");
        }
        return trips.stream()
                .filter(trip -> runsOnRoute(trip, from, to) && runsOn(trip, date))
                .collect(Collectors.toList());
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.trim();
    }

    private static boolean runsOnRoute(Trip trip, String from, String to) {
        return trip.getOrigin().equalsIgnoreCase(from) && trip.getDestination().equalsIgnoreCase(to);
    }

    private static boolean runsOn(Trip trip, LocalDate date) {
        return trip.getDeparture().toLocalDate().equals(date);
    }
}
