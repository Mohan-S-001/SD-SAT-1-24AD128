package com.ridesharing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RequestRideDto {
    @NotBlank(message = "riderId is required")
    private String riderId;

    @NotBlank(message = "vehicleType is required (e.g. SEDAN, MINI, SUV)")
    private String vehicleType;

    @Valid
    @NotNull(message = "pickupLocation is required")
    private LocationDto pickupLocation;

    @Valid
    @NotNull(message = "dropLocation is required")
    private LocationDto dropLocation;

    private String paymentMethod;

    public RequestRideDto() {}

    public RequestRideDto(String riderId, String vehicleType, LocationDto pickupLocation, LocationDto dropLocation, String paymentMethod) {
        this.riderId = riderId;
        this.vehicleType = vehicleType;
        this.pickupLocation = pickupLocation;
        this.dropLocation = dropLocation;
        this.paymentMethod = paymentMethod;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
