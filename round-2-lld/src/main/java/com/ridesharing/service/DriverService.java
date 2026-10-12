package com.ridesharing.service;

import com.ridesharing.dto.DriverDto;
import com.ridesharing.exception.ResourceNotFoundException;
import com.ridesharing.model.Driver;
import com.ridesharing.model.DriverStatus;
import com.ridesharing.model.Location;
import com.ridesharing.repository.InMemoryDriverRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DriverService {
    private final InMemoryDriverRepository driverRepository;

    public DriverService(InMemoryDriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public List<DriverDto> getNearbyDrivers(double latitude, double longitude, double radiusKm) {
        Location center = new Location("Query Center", latitude, longitude);
        List<Driver> nearby = driverRepository.findNearbyAvailable(center, radiusKm);

        return nearby.stream()
                .map(driver -> DriverDto.fromEntity(driver, driver.getCurrentLocation().distanceTo(center)))
                .collect(Collectors.toList());
    }

    public Driver getDriverById(String driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with id: " + driverId));
    }

    public DriverDto getDriverDto(String driverId) {
        Driver driver = getDriverById(driverId);
        return DriverDto.fromEntity(driver, 0.0);
    }

    public void updateDriverStatus(String driverId, DriverStatus status) {
        Driver driver = getDriverById(driverId);
        driver.setStatus(status);
        driverRepository.save(driver);
    }
}
