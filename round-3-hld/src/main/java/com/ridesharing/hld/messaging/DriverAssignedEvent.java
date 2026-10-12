package com.ridesharing.hld.messaging;

import java.io.Serializable;
import java.time.Instant;

/**
 * Event published to Kafka topic: 'driver.dispatched'
 */
public class DriverAssignedEvent implements Serializable {
    private String eventId;
    private String rideId;
    private String driverId;
    private String riderId;
    private double driverLatitude;
    private double driverLongitude;
    private double etaMinutes;
    private Instant timestamp;

    public DriverAssignedEvent() {
        this.timestamp = Instant.now();
    }

    public DriverAssignedEvent(String eventId, String rideId, String driverId, String riderId,
                               double driverLat, double driverLon, double etaMinutes) {
        this.eventId = eventId;
        this.rideId = rideId;
        this.driverId = driverId;
        this.riderId = riderId;
        this.driverLatitude = driverLat;
        this.driverLongitude = driverLon;
        this.etaMinutes = etaMinutes;
        this.timestamp = Instant.now();
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getRideId() { return rideId; }
    public void setRideId(String rideId) { this.rideId = rideId; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public double getDriverLatitude() { return driverLatitude; }
    public void setDriverLatitude(double driverLatitude) { this.driverLatitude = driverLatitude; }

    public double getDriverLongitude() { return driverLongitude; }
    public void setDriverLongitude(double driverLongitude) { this.driverLongitude = driverLongitude; }

    public double getEtaMinutes() { return etaMinutes; }
    public void setEtaMinutes(double etaMinutes) { this.etaMinutes = etaMinutes; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
