package com.ridesharing.dto;

import com.ridesharing.model.Ride;
import com.ridesharing.model.RideStatus;

import java.time.Instant;

public class RideResponseDto {
    private String rideId;
    private String riderId;
    private String driverId;
    private RideStatus status;
    private LocationDto pickupLocation;
    private LocationDto dropLocation;
    private String vehicleType;
    private double estimatedFare;
    private Double finalFare;
    private String otp;
    private Instant createdAt;
    private DriverDto assignedDriver;

    public RideResponseDto() {}

    public static RideResponseDto fromEntity(Ride ride, DriverDto driverDto) {
        RideResponseDto dto = new RideResponseDto();
        dto.setRideId(ride.getId());
        dto.setRiderId(ride.getRiderId());
        dto.setDriverId(ride.getDriverId());
        dto.setStatus(ride.getStatus());
        if (ride.getPickupLocation() != null) {
            dto.setPickupLocation(new LocationDto(
                    ride.getPickupLocation().getAddress(),
                    ride.getPickupLocation().getLatitude(),
                    ride.getPickupLocation().getLongitude()
            ));
        }
        if (ride.getDropLocation() != null) {
            dto.setDropLocation(new LocationDto(
                    ride.getDropLocation().getAddress(),
                    ride.getDropLocation().getLatitude(),
                    ride.getDropLocation().getLongitude()
            ));
        }
        dto.setVehicleType(ride.getVehicleType());
        dto.setEstimatedFare(ride.getEstimatedFare());
        dto.setFinalFare(ride.getFinalFare());
        dto.setOtp(ride.getOtp());
        dto.setCreatedAt(ride.getCreatedAt());
        dto.setAssignedDriver(driverDto);
        return dto;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public LocationDto getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(LocationDto pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public LocationDto getDropLocation() {
        return dropLocation;
    }

    public void setDropLocation(LocationDto dropLocation) {
        this.dropLocation = dropLocation;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getEstimatedFare() {
        return estimatedFare;
    }

    public void setEstimatedFare(double estimatedFare) {
        this.estimatedFare = estimatedFare;
    }

    public Double getFinalFare() {
        return finalFare;
    }

    public void setFinalFare(Double finalFare) {
        this.finalFare = finalFare;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public DriverDto getAssignedDriver() {
        return assignedDriver;
    }

    public void setAssignedDriver(DriverDto assignedDriver) {
        this.assignedDriver = assignedDriver;
    }
}
