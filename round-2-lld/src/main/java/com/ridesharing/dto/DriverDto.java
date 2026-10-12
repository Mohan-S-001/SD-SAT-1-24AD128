package com.ridesharing.dto;

import com.ridesharing.model.Driver;
import com.ridesharing.model.DriverStatus;

public class DriverDto {
    private String id;
    private String name;
    private String phone;
    private String vehicleNumber;
    private String vehicleModel;
    private double rating;
    private DriverStatus status;
    private LocationDto currentLocation;
    private double distanceKm;

    public DriverDto() {}

    public static DriverDto fromEntity(Driver driver, double distanceKm) {
        DriverDto dto = new DriverDto();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setPhone(driver.getPhone());
        dto.setVehicleNumber(driver.getVehicleNumber());
        dto.setVehicleModel(driver.getVehicleModel());
        dto.setRating(driver.getRating());
        dto.setStatus(driver.getStatus());
        if (driver.getCurrentLocation() != null) {
            dto.setCurrentLocation(new LocationDto(
                    driver.getCurrentLocation().getAddress(),
                    driver.getCurrentLocation().getLatitude(),
                    driver.getCurrentLocation().getLongitude()
            ));
        }
        dto.setDistanceKm(Math.round(distanceKm * 100.0) / 100.0);
        return dto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public LocationDto getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LocationDto currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }
}
