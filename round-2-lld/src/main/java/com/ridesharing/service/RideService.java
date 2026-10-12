package com.ridesharing.service;

import com.ridesharing.dto.CancelRideDto;
import com.ridesharing.dto.DriverDto;
import com.ridesharing.dto.RequestRideDto;
import com.ridesharing.dto.RideResponseDto;
import com.ridesharing.exception.InvalidRideStateException;
import com.ridesharing.exception.NoDriversAvailableException;
import com.ridesharing.exception.ResourceNotFoundException;
import com.ridesharing.model.*;
import com.ridesharing.repository.InMemoryRideRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class RideService {

    private final InMemoryRideRepository rideRepository;
    private final DriverService driverService;

    public RideService(InMemoryRideRepository rideRepository, DriverService driverService) {
        this.rideRepository = rideRepository;
        this.driverService = driverService;
    }

    /**
     * Task 1 & 3: Request Ride
     */
    public RideResponseDto requestRide(RequestRideDto requestDto) {
        Location pickup = new Location(
                requestDto.getPickupLocation().getAddress(),
                requestDto.getPickupLocation().getLatitude(),
                requestDto.getPickupLocation().getLongitude()
        );
        Location drop = new Location(
                requestDto.getDropLocation().getAddress(),
                requestDto.getDropLocation().getLatitude(),
                requestDto.getDropLocation().getLongitude()
        );

        // Pre-check: Ensure there are drivers available within 10 km
        List<DriverDto> nearbyDrivers = driverService.getNearbyDrivers(pickup.getLatitude(), pickup.getLongitude(), 10.0);
        if (nearbyDrivers.isEmpty()) {
            throw new NoDriversAvailableException("No available drivers found within a 10 km radius of pickup point.");
        }

        // Calculate distance and estimated fare
        double distanceKm = pickup.distanceTo(drop);
        if (distanceKm < 0.1) {
            throw new IllegalArgumentException("Drop location cannot be the same as pickup location.");
        }

        double baseFare = 50.0;
        double perKmRate = 18.0;
        double surgeMultiplier = 1.0;
        double estimatedFare = Math.round((baseFare + (distanceKm * perKmRate * surgeMultiplier)) * 100.0) / 100.0;

        String rideId = "ride_" + UUID.randomUUID().toString().substring(0, 8);
        Ride ride = new Ride(rideId, requestDto.getRiderId(), pickup, drop, requestDto.getVehicleType(), estimatedFare);
        ride.setStatus(RideStatus.SEARCHING_FOR_DRIVER);

        rideRepository.save(ride);
        return RideResponseDto.fromEntity(ride, null);
    }

    /**
     * Task 1: Accept Ride (Driver accepts)
     */
    public synchronized RideResponseDto acceptRide(String rideId, String driverId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.SEARCHING_FOR_DRIVER && ride.getStatus() != RideStatus.REQUESTED) {
            throw new InvalidRideStateException("Ride cannot be accepted. Current status is: " + ride.getStatus());
        }

        if (ride.getDriverId() != null) {
            throw new InvalidRideStateException("Ride has already been assigned to another driver.");
        }

        Driver driver = driverService.getDriverById(driverId);
        if (driver.getStatus() != DriverStatus.AVAILABLE) {
            throw new InvalidRideStateException("Driver is currently not available (Status: " + driver.getStatus() + ").");
        }

        // Assign driver and update states
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.DRIVER_ASSIGNED);
        ride.setAcceptedAt(Instant.now());
        rideRepository.save(ride);

        driverService.updateDriverStatus(driverId, DriverStatus.ON_TRIP);

        DriverDto driverDto = DriverDto.fromEntity(driver, driver.getCurrentLocation().distanceTo(ride.getPickupLocation()));
        return RideResponseDto.fromEntity(ride, driverDto);
    }

    /**
     * Task 1: Get Ride Status
     */
    public RideResponseDto getRideStatus(String rideId) {
        Ride ride = getRideEntity(rideId);
        DriverDto driverDto = null;
        if (ride.getDriverId() != null) {
            driverDto = driverService.getDriverDto(ride.getDriverId());
        }
        return RideResponseDto.fromEntity(ride, driverDto);
    }

    /**
     * Task 5: Start Ride (Driver enters rider OTP)
     */
    public RideResponseDto startRide(String rideId, String otp) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.DRIVER_ASSIGNED && ride.getStatus() != RideStatus.ARRIVED_AT_PICKUP) {
            throw new InvalidRideStateException("Cannot start ride. Ride is currently: " + ride.getStatus());
        }

        if (!ride.getOtp().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP provided for ride verification.");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(Instant.now());
        rideRepository.save(ride);

        DriverDto driverDto = driverService.getDriverDto(ride.getDriverId());
        return RideResponseDto.fromEntity(ride, driverDto);
    }

    /**
     * Task 5: Complete Ride
     */
    public RideResponseDto completeRide(String rideId) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new InvalidRideStateException("Cannot complete ride. Ride is not in progress (Current status: " + ride.getStatus() + ")");
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(Instant.now());
        ride.setFinalFare(ride.getEstimatedFare());
        rideRepository.save(ride);

        // Free up the driver
        if (ride.getDriverId() != null) {
            driverService.updateDriverStatus(ride.getDriverId(), DriverStatus.AVAILABLE);
        }

        DriverDto driverDto = driverService.getDriverDto(ride.getDriverId());
        return RideResponseDto.fromEntity(ride, driverDto);
    }

    /**
     * Task 6: Cancel Ride
     */
    public RideResponseDto cancelRide(String rideId, CancelRideDto cancelDto) {
        Ride ride = getRideEntity(rideId);

        if (ride.getStatus() == RideStatus.COMPLETED) {
            throw new InvalidRideStateException("Cannot cancel a ride that has already been completed.");
        }
        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new InvalidRideStateException("Ride is already cancelled.");
        }

        // Apply cancellation fee if cancelled after driver assignment
        double cancellationFee = 0.0;
        if (ride.getStatus() == RideStatus.DRIVER_ASSIGNED || ride.getStatus() == RideStatus.ARRIVED_AT_PICKUP) {
            cancellationFee = 50.0; // standard fee
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(Instant.now());
        ride.setCancelledBy(cancelDto.getCancelledBy());
        ride.setCancellationReason(cancelDto.getCancellationReason());
        ride.setCancellationFee(cancellationFee);
        rideRepository.save(ride);

        // Release driver back to available pool
        if (ride.getDriverId() != null) {
            driverService.updateDriverStatus(ride.getDriverId(), DriverStatus.AVAILABLE);
        }

        DriverDto driverDto = ride.getDriverId() != null ? driverService.getDriverDto(ride.getDriverId()) : null;
        return RideResponseDto.fromEntity(ride, driverDto);
    }

    private Ride getRideEntity(String rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id: " + rideId));
    }
}
