package com.ridesharing.controller;

import com.ridesharing.dto.ApiResponse;
import com.ridesharing.dto.CancelRideDto;
import com.ridesharing.dto.RequestRideDto;
import com.ridesharing.dto.RideResponseDto;
import com.ridesharing.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rides")
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    /**
     * Task 1, 2, 3: Request Ride
     * POST /api/v1/rides/request
     * Status Code: 201 Created
     */
    @PostMapping("/request")
    public ResponseEntity<ApiResponse<RideResponseDto>> requestRide(@Valid @RequestBody RequestRideDto requestDto) {
        RideResponseDto rideResponse = rideService.requestRide(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED.value(),
                        "Ride requested successfully. Searching for nearby drivers.",
                        rideResponse
                ));
    }

    /**
     * Task 1 & 2: Accept Ride
     * PATCH /api/v1/rides/{rideId}/accept?driverId={driverId}
     * Status Code: 200 OK
     */
    @PatchMapping("/{rideId}/accept")
    public ResponseEntity<ApiResponse<RideResponseDto>> acceptRide(
            @PathVariable String rideId,
            @RequestParam String driverId) {

        RideResponseDto rideResponse = rideService.acceptRide(rideId, driverId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Ride accepted by driver " + driverId + ". Navigate to pickup location.",
                rideResponse
        ));
    }

    /**
     * Task 1 & 2: Get Ride Status
     * GET /api/v1/rides/{rideId}/status
     * Status Code: 200 OK
     */
    @GetMapping("/{rideId}/status")
    public ResponseEntity<ApiResponse<RideResponseDto>> getRideStatus(@PathVariable String rideId) {
        RideResponseDto rideResponse = rideService.getRideStatus(rideId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Ride status retrieved successfully.",
                rideResponse
        ));
    }

    /**
     * Task 5: Start Ride
     * POST /api/v1/rides/{rideId}/start?otp={otp}
     * Status Code: 200 OK
     */
    @PostMapping("/{rideId}/start")
    public ResponseEntity<ApiResponse<RideResponseDto>> startRide(
            @PathVariable String rideId,
            @RequestParam String otp) {

        RideResponseDto rideResponse = rideService.startRide(rideId, otp);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Ride started successfully.",
                rideResponse
        ));
    }

    /**
     * Task 5: Complete Ride
     * POST /api/v1/rides/{rideId}/complete
     * Status Code: 200 OK
     */
    @PostMapping("/{rideId}/complete")
    public ResponseEntity<ApiResponse<RideResponseDto>> completeRide(@PathVariable String rideId) {
        RideResponseDto rideResponse = rideService.completeRide(rideId);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Ride completed successfully.",
                rideResponse
        ));
    }

    /**
     * Task 6: Cancel Ride
     * POST /api/v1/rides/{rideId}/cancel
     * Status Code: 200 OK
     */
    @PostMapping("/{rideId}/cancel")
    public ResponseEntity<ApiResponse<RideResponseDto>> cancelRide(
            @PathVariable String rideId,
            @Valid @RequestBody CancelRideDto cancelDto) {

        RideResponseDto rideResponse = rideService.cancelRide(rideId, cancelDto);
        return ResponseEntity.ok(ApiResponse.success(
                HttpStatus.OK.value(),
                "Ride has been cancelled.",
                rideResponse
        ));
    }
}
