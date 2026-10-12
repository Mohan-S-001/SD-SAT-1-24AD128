package com.ridesharing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RideSharingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RideSharingApplication.class, args);
        System.out.println("===============================================================");
        System.out.println(" Ride-Sharing & Driver Dispatch Platform API Server Started ");
        System.out.println(" Base URL: http://localhost:8080/api/v1                        ");
        System.out.println("===============================================================");
    }
}
