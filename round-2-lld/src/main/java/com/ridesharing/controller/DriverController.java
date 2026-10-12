package com.ridesharing.controller;

import com.ridesharing.dto.ApiResponse;
import com.ridesharing.dto.DriverDto;
import com.ridesharing.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/drivers")
@CrossOrigin(origins = "*")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    /**
     * Task 1 & 2: Get Nearby Drivers
     * GET /api/v1/drivers/nearby?latitude=19.0896&longitude=72.8656&radiusKm=5.0
     */
    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<List<DriverDto>>> getNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radiusKm) {

        List<DriverDto> drivers = driverService.getNearbyDrivers(latitude, longitude, radiusKm);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Found " + drivers.size() + " drivers within " + radiusKm + " km radius.",
                drivers
        ));
    }
}
