package com.ridesharing.hld.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * High-Throughput Event Publisher for Apache Kafka.
 * Partitioning strategy: Routes events to Kafka partitions by Geohash Prefix
 * ensuring ordered consumption for specific geographic city zones.
 */
public class RideEventKafkaPublisher {
    private static final Logger log = LoggerFactory.getLogger(RideEventKafkaPublisher.class);

    public static final String TOPIC_RIDE_REQUESTED = "ride.requested";
    public static final String TOPIC_DRIVER_DISPATCHED = "driver.dispatched";
    public static final String TOPIC_RIDE_STATUS_UPDATED = "ride.status_updated";

    public void publishRideRequested(RideRequestedEvent event) {
        // In production: kafkaTemplate.send(TOPIC_RIDE_REQUESTED, event.getGeohashPartitionKey(), event);
        log.info("[KAFKA PRODUCER] Published to Topic '{}' | Key: '{}' | RideId: '{}' | Rider: '{}'",
                TOPIC_RIDE_REQUESTED, event.getGeohashPartitionKey(), event.getRideId(), event.getRiderId());
    }

    public void publishDriverAssigned(DriverAssignedEvent event) {
        // In production: kafkaTemplate.send(TOPIC_DRIVER_DISPATCHED, event.getRideId(), event);
        log.info("[KAFKA PRODUCER] Published to Topic '{}' | RideId: '{}' | DriverId: '{}' | ETA: {} mins",
                TOPIC_DRIVER_DISPATCHED, event.getRideId(), event.getDriverId(), event.getEtaMinutes());
    }
}
