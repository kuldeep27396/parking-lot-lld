package com.lld.parkinglot.controller;

import com.lld.parkinglot.service.ParkingLotService;
import com.lld.parkinglot.factory.VehicleFactory;
import com.lld.parkinglot.models.*;
import com.lld.parkinglot.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

/**
 * REST CONTROLLER - Demonstrates FACADE PATTERN
 * 
 * This controller provides a simple REST API interface to the complex
 * parking lot system. It demonstrates:
 * 
 * 1. FACADE PATTERN: Simple interface to complex subsystem
 * 2. DEPENDENCY INJECTION: Spring manages dependencies
 * 3. SINGLE RESPONSIBILITY: Only handles HTTP requests/responses
 */
@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "*")
public class ParkingLotController {
    
    // DEPENDENCY INJECTION: Spring automatically injects dependencies
    @Autowired
    private ParkingLotService parkingLotService;
    
    @Autowired
    private VehicleFactory vehicleFactory;
    
    /**
     * ENDPOINT: Park a vehicle
     * Demonstrates FACADE pattern - simple interface to complex operation
     */
    @PostMapping("/enter")
    public ResponseEntity<?> parkVehicle(@RequestBody ParkVehicleRequest request) {
        try {
            // VALIDATION
            if (!vehicleFactory.isValidVehicleData(request.getVehicleType(), 
                    request.getLicenseNumber(), request.getColor(), request.getModel())) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Invalid vehicle data provided"));
            }
            
            // FACTORY PATTERN: Create vehicle using factory
            Vehicle vehicle = vehicleFactory.createVehicle(
                request.getVehicleType(),
                request.getLicenseNumber(),
                request.getColor(),
                request.getModel(),
                request.getAdditionalParams()
            );
            
            // BUSINESS LOGIC: Park the vehicle
            ParkingTicket ticket = parkingLotService.parkVehicle(vehicle);
            
            if (ticket == null) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("No available parking spots for this vehicle type"));
            }
            
            return ResponseEntity.ok(new ParkVehicleResponse(ticket));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Error parking vehicle: " + e.getMessage()));
        }
    }
    
    /**
     * ENDPOINT: Exit vehicle and process payment
     */
    @PostMapping("/exit")
    public ResponseEntity<?> exitVehicle(@RequestBody ExitVehicleRequest request) {
        try {
            // Find active ticket
            List<ParkingTicket> activeTickets = parkingLotService.getActiveTickets();
            ParkingTicket ticket = activeTickets.stream()
                .filter(t -> t.getTicketNumber().equals(request.getTicketNumber()))
                .findFirst()
                .orElse(null);
            
            if (ticket == null) {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Invalid ticket number"));
            }
            
            // Calculate charges
            double charges = parkingLotService.calculateParkingCharges(ticket);
            
            // Process exit
            boolean exitSuccessful = parkingLotService.unparkVehicle(ticket);
            
            if (exitSuccessful) {
                return ResponseEntity.ok(new ExitVehicleResponse(ticket, charges));
            } else {
                return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Failed to process vehicle exit"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(new ErrorResponse("Error processing vehicle exit: " + e.getMessage()));
        }
    }
    
    /**
     * ENDPOINT: Get parking lot status
     */
    @GetMapping("/status")
    public ResponseEntity<ParkingLotStatus> getParkingLotStatus() {
        Map<ParkingSpotType, Long> availableSpots = parkingLotService.getAvailableSpotCount();
        double occupancyRate = parkingLotService.getOccupancyRate();
        int activeTicketCount = parkingLotService.getActiveTickets().size();
        
        return ResponseEntity.ok(new ParkingLotStatus(availableSpots, occupancyRate, activeTicketCount));
    }
    
    /**
     * ENDPOINT: Get available spots by type
     */
    @GetMapping("/spots/available")
    public ResponseEntity<Map<ParkingSpotType, Long>> getAvailableSpots() {
        return ResponseEntity.ok(parkingLotService.getAvailableSpotCount());
    }
    
    /**
     * ENDPOINT: Reserve a parking spot
     */
    @PostMapping("/reserve")
    public ResponseEntity<?> reserveSpot(@RequestBody ReserveSpotRequest request) {
        boolean reserved = parkingLotService.reserveSpot(
            request.getSpotNumber(), 
            request.getDurationMinutes()
        );
        
        if (reserved) {
            return ResponseEntity.ok(new SuccessResponse("Spot reserved successfully"));
        } else {
            return ResponseEntity.badRequest()
                .body(new ErrorResponse("Failed to reserve spot"));
        }
    }
    
    // DTO CLASSES for Request/Response
    
    public static class ParkVehicleRequest {
        private VehicleType vehicleType;
        private String licenseNumber;
        private String color;
        private String model;
        private Object[] additionalParams;
        
        // Getters
        public VehicleType getVehicleType() { return vehicleType; }
        public String getLicenseNumber() { return licenseNumber; }
        public String getColor() { return color; }
        public String getModel() { return model; }
        public Object[] getAdditionalParams() { return additionalParams; }
        
        // Setters  
        public void setVehicleType(VehicleType vehicleType) { this.vehicleType = vehicleType; }
        public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
        public void setColor(String color) { this.color = color; }
        public void setModel(String model) { this.model = model; }
        public void setAdditionalParams(Object[] additionalParams) { this.additionalParams = additionalParams; }
    }
    
    public static class ParkVehicleResponse {
        private String ticketNumber;
        private String spotNumber;
        private String entryTime;
        private String vehicleInfo;
        
        public ParkVehicleResponse(ParkingTicket ticket) {
            this.ticketNumber = ticket.getTicketNumber();
            this.spotNumber = ticket.getParkingSpot().getSpotNumber();
            this.entryTime = ticket.getEntryTime().toString();
            this.vehicleInfo = ticket.getVehicle().getDisplayInfo();
        }
    }
    
    public static class ExitVehicleRequest {
        private String ticketNumber;
    
        public String getTicketNumber() { return ticketNumber; }
        public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
    }
    
    public static class ExitVehicleResponse {
        private String ticketNumber;
        private String exitTime;
        private long parkingDurationHours;
        private double charges;
        private String vehicleInfo;
        
        public ExitVehicleResponse(ParkingTicket ticket, double charges) {
            this.ticketNumber = ticket.getTicketNumber();
            this.exitTime = ticket.getExitTime().toString();
            this.parkingDurationHours = ticket.getParkingDurationHours();
            this.charges = charges;
            this.vehicleInfo = ticket.getVehicle().getDisplayInfo();
        }
    }
    
    public static class ParkingLotStatus {
        private Map<ParkingSpotType, Long> availableSpots;
        private double occupancyRate;
        private int activeTickets;
        
        public ParkingLotStatus(Map<ParkingSpotType, Long> availableSpots, double occupancyRate, int activeTickets) {
            this.availableSpots = availableSpots;
            this.occupancyRate = occupancyRate;
            this.activeTickets = activeTickets;
        }
        
        public Map<ParkingSpotType, Long> getAvailableSpots() { return availableSpots; }
        public double getOccupancyRate() { return occupancyRate; }
        public int getActiveTickets() { return activeTickets; }
    }
    
    public static class ReserveSpotRequest {
        private String spotNumber;
        private int durationMinutes;
    
        public String getSpotNumber() { return spotNumber; }
        public int getDurationMinutes() { return durationMinutes; }
        public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
        public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    }
    
    public static class ErrorResponse {
        private String error;
        private long timestamp = System.currentTimeMillis();
        
        public ErrorResponse(String error) {
            this.error = error;
        }
    }
    
    public static class SuccessResponse {
        private String message;
        private long timestamp = System.currentTimeMillis();
        
        public SuccessResponse(String message) {
            this.message = message;
        }
    }
}
