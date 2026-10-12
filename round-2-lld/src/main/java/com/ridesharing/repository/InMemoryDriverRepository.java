package com.ridesharing.repository;

import com.ridesharing.model.Driver;
import com.ridesharing.model.DriverStatus;
import com.ridesharing.model.Location;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDriverRepository {
    private final Map<String, Driver> driverStorage = new ConcurrentHashMap<>();

    public InMemoryDriverRepository() {
        // Seed initial sample drivers for demonstration
        seedInitialDrivers();
    }

    private void seedInitialDrivers() {
        save(new Driver("drv_101", "Rajesh Sharma", "+919876543210", "MH02-AB-1234", "Toyota Innova", 4.9,
                DriverStatus.AVAILABLE, new Location("Near Airport Terminal 2", 19.0910, 72.8640)));

        save(new Driver("drv_102", "Amit Patel", "+919876543211", "MH01-CD-5678", "Maruti Dzire", 4.8,
                DriverStatus.AVAILABLE, new Location("Andheri East Metro", 19.0980, 72.8590)));

        save(new Driver("drv_103", "Vikram Singh", "+919876543212", "MH04-EF-9012", "Hyundai Aura", 4.7,
                DriverStatus.AVAILABLE, new Location("Santacruz Flyover", 19.0830, 72.8520)));

        save(new Driver("drv_104", "Suresh Nair", "+919876543213", "MH03-GH-3456", "Honda City", 4.6,
                DriverStatus.ON_TRIP, new Location("BKC Main Avenue", 19.0665, 72.8680)));
    }

    public Driver save(Driver driver) {
        driverStorage.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> findById(String driverId) {
        return Optional.ofNullable(driverStorage.get(driverId));
    }

    public List<Driver> findAll() {
        return new ArrayList<>(driverStorage.values());
    }

    public List<Driver> findNearbyAvailable(Location center, double radiusKm) {
        List<Driver> nearby = new ArrayList<>();
        for (Driver driver : driverStorage.values()) {
            if (driver.getStatus() == DriverStatus.AVAILABLE && driver.getCurrentLocation() != null) {
                double distance = driver.getCurrentLocation().distanceTo(center);
                if (distance <= radiusKm) {
                    nearby.add(driver);
                }
            }
        }
        nearby.sort(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(center)));
        return nearby;
    }
}
