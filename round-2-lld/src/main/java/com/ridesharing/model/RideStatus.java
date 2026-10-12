package com.ridesharing.model;

public enum RideStatus {
    REQUESTED,
    SEARCHING_FOR_DRIVER,
    DRIVER_ASSIGNED,
    ARRIVED_AT_PICKUP,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}
