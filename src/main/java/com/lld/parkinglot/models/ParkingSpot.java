package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.ParkingSpotType;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * PARKING SPOT MODEL - Demonstrates ENCAPSULATION and STATE PATTERN
 * 
 * This class represents a physical parking spot in the parking lot.
 * 
 * DESIGN PRINCIPLES:
 * 1. ENCAPSULATION: Private state with controlled access
 * 2. STATE PATTERN: Different behaviors based on current state
 * 3. SINGLE RESPONSIBILITY: Only manages parking spot state and operations
 */
@Entity
@Table(name = "parking_spots")
public class ParkingSpot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String spotNumber;
    
    @Enumerated(EnumType.STRING)
    private ParkingSpotType spotType;
    
    private int floorLevel;
    
    private String section;
    
    private boolean isAvailable = true;
    
    private boolean available = true;
    
    private boolean isReserved = false;
    
    private boolean reserved = false;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_vehicle_id")
    private Vehicle currentVehicle;
    
    private LocalDateTime occupiedSince;
    
    private LocalDateTime reservedUntil;
    
    /**
     * Default constructor for JPA
     */
    public ParkingSpot() {}
    
    /**
     * Constructor for creating new parking spots
     */
    public ParkingSpot(String spotNumber, ParkingSpotType spotType, int floorLevel) {
        this.spotNumber = spotNumber;
        this.spotType = spotType;
        this.floorLevel = floorLevel;
        this.isAvailable = true;
        this.isReserved = false;
    }
    
    /**
     * STATE PATTERN: Behavior changes based on current state
     * 
     * Check if this spot can accommodate a specific vehicle
     */
    public boolean canAccommodate(Vehicle vehicle) {
        if (!isAvailable || isReserved) {
            return false;
        }
        
        // Delegate to spot type for accommodation logic
        return spotType.canAccommodate(vehicle.getVehicleType());
    }
    
    /**
     * ENCAPSULATION: Controlled state change method
     * 
     * Park a vehicle in this spot
     */
    public boolean parkVehicle(Vehicle vehicle) {
        if (!canAccommodate(vehicle)) {
            return false;
        }
        
        // STATE CHANGE: Available -> Occupied
        this.currentVehicle = vehicle;
        this.isAvailable = false;
        this.occupiedSince = LocalDateTime.now();
        
        return true;
    }
    
    /**
     * ENCAPSULATION: Controlled state change method
     * 
     * Remove vehicle from this spot
     */
    public boolean unparkVehicle() {
        if (isAvailable || currentVehicle == null) {
            return false;
        }
        
        // STATE CHANGE: Occupied -> Available
        this.currentVehicle = null;
        this.isAvailable = true;
        this.occupiedSince = null;
        
        return true;
    }
    
    /**
     * Reserve this spot for a specific duration
     */
    public boolean reserve(int minutes) {
        if (!isAvailable) {
            return false;
        }
        
        this.isReserved = true;
        this.reservedUntil = LocalDateTime.now().plusMinutes(minutes);
        return true;
    }
    
    /**
     * Check if reservation has expired
     */
    public boolean isReservationExpired() {
        return isReserved && 
               reservedUntil != null && 
               LocalDateTime.now().isAfter(reservedUntil);
    }
    
    /**
     * Clear expired reservations
     */
    public void clearExpiredReservation() {
        if (isReservationExpired()) {
            this.isReserved = false;
            this.reservedUntil = null;
        }
    }
    
    /**
     * Get current status as string (useful for display)
     */
    public String getStatus() {
        if (!isAvailable && currentVehicle != null) {
            return "OCCUPIED by " + currentVehicle.getLicenseNumber();
        } else if (isReserved) {
            return "RESERVED until " + reservedUntil;
        } else {
            return "AVAILABLE";
        }
    }
    
    /**
     * Calculate parking duration in minutes
     */
    public long getParkingDurationMinutes() {
        if (occupiedSince == null) {
            return 0;
        }
        return java.time.Duration.between(occupiedSince, LocalDateTime.now()).toMinutes();
    }
    
    // ENCAPSULATION: Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSpotNumber() { return spotNumber; }
    public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }
    
    public ParkingSpotType getSpotType() { return spotType; }
    public void setSpotType(ParkingSpotType spotType) { this.spotType = spotType; }
    
    public int getFloorLevel() { return floorLevel; }
    public void setFloorLevel(int floorLevel) { this.floorLevel = floorLevel; }
    
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    
    public Vehicle getCurrentVehicle() { return currentVehicle; }
    public void setCurrentVehicle(Vehicle currentVehicle) { this.currentVehicle = currentVehicle; }
    
    public LocalDateTime getOccupiedSince() { return occupiedSince; }
    public void setOccupiedSince(LocalDateTime occupiedSince) { this.occupiedSince = occupiedSince; }
    
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    
    public LocalDateTime getReservedUntil() { return reservedUntil; }
    public void setReservedUntil(LocalDateTime reservedUntil) { this.reservedUntil = reservedUntil; }
}
