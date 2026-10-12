package com.ridesharing.repository;

import com.ridesharing.model.Ride;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRideRepository {
    private final Map<String, Ride> rideStorage = new ConcurrentHashMap<>();

    public Ride save(Ride ride) {
        rideStorage.put(ride.getId(), ride);
        return ride;
    }

    public Optional<Ride> findById(String rideId) {
        return Optional.ofNullable(rideStorage.get(rideId));
    }

    public boolean existsById(String rideId) {
        return rideStorage.containsKey(rideId);
    }
}
