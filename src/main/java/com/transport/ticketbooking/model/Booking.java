package com.transport.ticketbooking.model;

public class Booking {

    private final String id;
    private final Trip trip;
    private final String passengerName;
    private final int seats;

    public Booking(String id, Trip trip, String passengerName, int seats) {
        this.id = id;
        this.trip = trip;
        this.passengerName = passengerName;
        this.seats = seats;
    }

    public String getId() {
        return id;
    }

    public Trip getTrip() {
        return trip;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public int getSeats() {
        return seats;
    }
}
