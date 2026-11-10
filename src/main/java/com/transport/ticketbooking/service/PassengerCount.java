package com.transport.ticketbooking.service;

/** Single source of truth for the allowed number of passengers per booking. */
final class PassengerCount {

    static final int MIN = 1;
    static final int MAX = 10;

    private PassengerCount() {
    }

    static int require(int passengers) {
        if (passengers < MIN || passengers > MAX) {
            throw new IllegalArgumentException(
                    "passengers must be between " + MIN + " and " + MAX + " but was " + passengers);
        }
        return passengers;
    }
}
