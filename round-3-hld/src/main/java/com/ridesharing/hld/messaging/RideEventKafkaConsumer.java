package com.ridesharing.hld.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Event-Driven Kafka Consumer representing the Matching Engine & Notification worker services.
 */
public class RideEventKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(RideEventKafkaConsumer.class);

    /**
     * Consumes new ride requests from Kafka and triggers candidate driver matching.
     */
    public void consumeRideRequested(RideRequestedEvent event) {
        log.info("[KAFKA CONSUMER - MATCHING ENGINE] Processing RideRequestedEvent | Ride: {} | Geo: ({}, {})",
                event.getRideId(), event.getPickupLatitude(), event.getPickupLongitude());

        // Step 1: Query Geospatial In-Memory Cache (Redis) for nearby available drivers
        // Step 2: Calculate ETA via Routing Engine
        // Step 3: Broadcast dispatch notification to selected candidate drivers
    }

    /**
     * Consumes driver assignment events to push real-time alerts to the rider.
     */
    public void consumeDriverAssigned(DriverAssignedEvent event) {
        log.info("[KAFKA CONSUMER - NOTIFICATION SVC] Dispatched Push Notification to Rider {} | Driver {} is on the way (ETA: {} mins)",
                event.getRiderId(), event.getDriverId(), event.getEtaMinutes());
    }
}
