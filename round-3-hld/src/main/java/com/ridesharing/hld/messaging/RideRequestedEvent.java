package com.ridesharing.hld.messaging;

import java.io.Serializable;
import java.time.Instant;

/**
 * Event published to Kafka topic: 'ride.requested'
 */
public class RideRequestedEvent implements Serializable {
    private String eventId;
    private String rideId;
    private String riderId;
    private double pickupLatitude;
    private double pickupLongitude;
    private double dropLatitude;
    private double dropLongitude;
    private String vehicleType;
    private double estimatedFare;
    private String geohashPartitionKey;
    private Instant timestamp;

    public RideRequestedEvent() {
        this.timestamp = Instant.now();
    }

    public RideRequestedEvent(String eventId, String rideId, String riderId, double pickupLat, double pickupLon,
                              double dropLat, double dropLon, String vehicleType, double estimatedFare, String geohash) {
        this.eventId = eventId;
        this.rideId = rideId;
        this.riderId = riderId;
        this.pickupLatitude = pickupLat;
        this.pickupLongitude = pickupLon;
        this.dropLatitude = dropLat;
        this.dropLongitude = dropLon;
        this.vehicleType = vehicleType;
        this.estimatedFare = estimatedFare;
        this.geohashPartitionKey = geohash;
        this.timestamp = Instant.now();
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public String getRideId() { return rideId; }
    public void setRideId(String rideId) { this.rideId = rideId; }

    public String getRiderId() { return riderId; }
    public void setRiderId(String riderId) { this.riderId = riderId; }

    public double getPickupLatitude() { return pickupLatitude; }
    public void setPickupLatitude(double pickupLatitude) { this.pickupLatitude = pickupLatitude; }

    public double getPickupLongitude() { return pickupLongitude; }
    public void setPickupLongitude(double pickupLongitude) { this.pickupLongitude = pickupLongitude; }

    public double getDropLatitude() { return dropLatitude; }
    public void setDropLatitude(double dropLatitude) { this.dropLatitude = dropLatitude; }

    public double getDropLongitude() { return dropLongitude; }
    public void setDropLongitude(double dropLongitude) { this.dropLongitude = dropLongitude; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public double getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(double estimatedFare) { this.estimatedFare = estimatedFare; }

    public String getGeohashPartitionKey() { return geohashPartitionKey; }
    public void setGeohashPartitionKey(String geohashPartitionKey) { this.geohashPartitionKey = geohashPartitionKey; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
