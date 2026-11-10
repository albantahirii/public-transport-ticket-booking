package com.transport.ticketbooking.service;

import com.transport.ticketbooking.model.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RouteSearchService {

    private final List<Trip> trips;

    public RouteSearchService(List<Trip> trips) {
        this.trips = trips;
    }

    public List<Trip> search(String origin, String destination, LocalDate date) {
        if (origin == null || origin.isBlank() || destination == null || destination.isBlank()
                || date == null) {
            throw new IllegalArgumentException("origin, destination and date are required");
        }
        String from = origin.trim();
        String to = destination.trim();
        if (from.equalsIgnoreCase(to)) {
            throw new IllegalArgumentException("origin and destination must differ");
        }
        List<Trip> result = new ArrayList<>();
        for (Trip trip : trips) {
            if (trip.getOrigin().equalsIgnoreCase(from)
                    && trip.getDestination().equalsIgnoreCase(to)
                    && trip.getDeparture().toLocalDate().equals(date)) {
                result.add(trip);
            }
        }
        return result;
    }
}
