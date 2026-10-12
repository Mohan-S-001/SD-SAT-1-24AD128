package com.ridesharing.service;

import com.ridesharing.dto.CancelRideDto;
import com.ridesharing.dto.LocationDto;
import com.ridesharing.dto.RequestRideDto;
import com.ridesharing.dto.RideResponseDto;
import com.ridesharing.model.RideStatus;
import com.ridesharing.repository.InMemoryDriverRepository;
import com.ridesharing.repository.InMemoryRideRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RideServiceTest {

    private RideService rideService;
    private DriverService driverService;
    private InMemoryRideRepository rideRepository;
    private InMemoryDriverRepository driverRepository;

    @BeforeEach
    public void setUp() {
        rideRepository = new InMemoryRideRepository();
        driverRepository = new InMemoryDriverRepository();
        driverService = new DriverService(driverRepository);
        rideService = new RideService(rideRepository, driverService);
    }

    @Test
    public void testRequestRide_Success() {
        RequestRideDto requestDto = new RequestRideDto(
                "usr_test_1",
                "SEDAN",
                new LocationDto("Airport Terminal 2", 19.0896, 72.8656),
                new LocationDto("Bandra Kurla Complex", 19.0660, 72.8677),
                "CREDIT_CARD"
        );

        RideResponseDto response = rideService.requestRide(requestDto);

        assertNotNull(response.getRideId());
        assertEquals("usr_test_1", response.getRiderId());
        assertEquals(RideStatus.SEARCHING_FOR_DRIVER, response.getStatus());
        assertTrue(response.getEstimatedFare() > 50.0);
    }

    @Test
    public void testFullRideLifecycle_RequestAcceptStartComplete() {
        // 1. Request
        RequestRideDto requestDto = new RequestRideDto(
                "usr_test_2",
                "SEDAN",
                new LocationDto("Airport Terminal 2", 19.0896, 72.8656),
                new LocationDto("Andheri East", 19.1136, 72.8697),
                "CASH"
        );
        RideResponseDto requested = rideService.requestRide(requestDto);
        String rideId = requested.getRideId();

        // 2. Accept by driver drv_101
        RideResponseDto accepted = rideService.acceptRide(rideId, "drv_101");
        assertEquals(RideStatus.DRIVER_ASSIGNED, accepted.getStatus());
        assertEquals("drv_101", accepted.getDriverId());
        assertNotNull(accepted.getAssignedDriver());

        // 3. Start Ride with OTP
        String otp = accepted.getOtp();
        RideResponseDto started = rideService.startRide(rideId, otp);
        assertEquals(RideStatus.IN_PROGRESS, started.getStatus());

        // 4. Complete Ride
        RideResponseDto completed = rideService.completeRide(rideId);
        assertEquals(RideStatus.COMPLETED, completed.getStatus());
        assertNotNull(completed.getFinalFare());
    }

    @Test
    public void testCancelRide_Success() {
        RequestRideDto requestDto = new RequestRideDto(
                "usr_test_3",
                "SEDAN",
                new LocationDto("Airport Terminal 2", 19.0896, 72.8656),
                new LocationDto("Bandra Kurla Complex", 19.0660, 72.8677),
                "UPI"
        );
        RideResponseDto requested = rideService.requestRide(requestDto);
        String rideId = requested.getRideId();

        // Cancel while searching
        CancelRideDto cancelDto = new CancelRideDto("RIDER", "CHANGED_MIND", "No longer traveling today");
        RideResponseDto cancelled = rideService.cancelRide(rideId, cancelDto);

        assertEquals(RideStatus.CANCELLED, cancelled.getStatus());
    }
}
