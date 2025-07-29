package com.lld.parkinglot.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

/**
 * PARKING TICKET MODEL - Demonstrates ENCAPSULATION and VALUE OBJECT pattern
 * 
 * This class represents a parking ticket issued when a vehicle enters the parking lot.
 * 
 * DESIGN PRINCIPLES:
 * 1. IMMUTABILITY: Once created, core details shouldn't change
 * 2. ENCAPSULATION: Private fields with controlled access
 * 3. VALUE OBJECT: Contains data and behavior related to parking session
 */
@Entity
@Table(name = "parking_tickets")
public class ParkingTicket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String ticketNumber;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;
    
    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;
    
    @Column(nullable = false)
    private LocalDateTime entryTime;
    
    private LocalDateTime exitTime;
    
    @Column(nullable = false)
    private boolean active = true;
    
    private Double totalAmount;
    
    private boolean isPaid;
    
    private String customerPhone;
    
    /**
     * Default constructor for JPA
     */
    public ParkingTicket() {}
    
    /**
     * Constructor for creating new parking tickets
     * Demonstrates ENCAPSULATION - controlled object creation
     */
    public ParkingTicket(String ticketNumber, Vehicle vehicle, ParkingSpot parkingSpot, String customerPhone) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
        this.parkingSpot = parkingSpot;
        this.customerPhone = customerPhone;
        this.entryTime = LocalDateTime.now();
        this.isPaid = false;
    }
    
    /**
     * BUSINESS LOGIC: Calculate parking duration
     * 
     * Returns parking duration in minutes.
     * If vehicle hasn't exited yet, calculates duration till now.
     */
    public long getParkingDurationMinutes() {
        LocalDateTime endTime = exitTime != null ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, endTime).toMinutes();
    }
    
    /**
     * BUSINESS LOGIC: Calculate parking duration in hours (ceiling)
     * 
     * Parking is typically charged per hour, so we round up.
     * This demonstrates SINGLE RESPONSIBILITY - this method has one clear purpose.
     */
    public long getParkingDurationHours() {
        long minutes = getParkingDurationMinutes();
        return (long) Math.ceil(minutes / 60.0);
    }
    
    /**
     * ENCAPSULATION: Controlled state change
     * 
     * Mark vehicle as exited and record exit time
     */
    public void markExit() {
        if (exitTime == null) { // Prevent multiple exits
            this.exitTime = LocalDateTime.now();
        }
    }
    
    /**
     * BUSINESS LOGIC: Check if ticket is expired
     * 
     * A ticket might be considered expired if vehicle stays too long
     */
    public boolean isExpired(int maxHours) {
        return getParkingDurationHours() > maxHours;
    }
    
    /**
     * ENCAPSULATION: Controlled payment processing
     */
    public boolean markAsPaid(double amount) {
        if (amount <= 0) {
            return false;
        }
        
        this.totalAmount = amount;
        this.isPaid = true;
        return true;
    }
    
    /**
     * BUSINESS LOGIC: Check if payment is required
     */
    public boolean requiresPayment() {
        return !isPaid && totalAmount != null && totalAmount > 0;
    }
    
    /**
     * UTILITY: Get formatted ticket information
     * Useful for displaying ticket details
     */
    public String getTicketSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("🎫 Ticket: ").append(ticketNumber).append("\n");
        summary.append("🚗 Vehicle: ").append(vehicle.getDisplayInfo()).append("\n");
        summary.append("🅿️ Spot: ").append(parkingSpot.getSpotNumber()).append(" (Floor ").append(parkingSpot.getFloorLevel()).append(")\n");
        summary.append("⏰ Entry: ").append(entryTime).append("\n");
        
        if (exitTime != null) {
            summary.append("🚪 Exit: ").append(exitTime).append("\n");
        }
        
        summary.append("⏱️ Duration: ").append(getParkingDurationHours()).append(" hours\n");
        
        if (totalAmount != null) {
            summary.append("💰 Amount: $").append(String.format("%.2f", totalAmount)).append("\n");
            summary.append("💳 Paid: ").append(isPaid ? "✅" : "❌").append("\n");
        }
        
        return summary.toString();
    }
    
    /**
     * VALIDATION: Check if ticket is valid for operations
     */
    public boolean isValid() {
        return ticketNumber != null && 
               !ticketNumber.trim().isEmpty() &&
               vehicle != null && 
               parkingSpot != null && 
               entryTime != null;
    }
    
    // ENCAPSULATION: Getter and Setter methods
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }
    
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    
    public ParkingSpot getParkingSpot() { return parkingSpot; }
    public void setParkingSpot(ParkingSpot parkingSpot) { this.parkingSpot = parkingSpot; }
    
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }
    
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
