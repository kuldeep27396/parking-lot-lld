package com.lld.parkinglot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MAIN APPLICATION CLASS
 * 
 * This is the entry point of our Spring Boot application.
 * @SpringBootApplication annotation combines:
 * - @Configuration: Tags the class as a source of bean definitions
 * - @EnableAutoConfiguration: Enables Spring Boot's auto-configuration
 * - @ComponentScan: Enables component scanning in the current package
 */
@SpringBootApplication
public class ParkingLotApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ParkingLotApplication.class, args);
        System.out.println("🚗 Parking Lot System Started Successfully!");
        System.out.println("📖 Learning LLD & OOP with Spring Boot");
    }
}
