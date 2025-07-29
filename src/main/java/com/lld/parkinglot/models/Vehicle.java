package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.VehicleType;
import jakarta.persistence.*;

/**
 * ABSTRACT BASE CLASS demonstrating ABSTRACTION and INHERITANCE
 * 
 * This abstract class defines the common structure for all vehicles.
 * 
 * DESIGN PRINCIPLES DEMONSTRATED:
 * 1. ABSTRACTION: Defines what a vehicle should have without implementation details
 * 2. ENCAPSULATION: Private fields with controlled access
 * 3. TEMPLATE METHOD PATTERN: Common structure for all vehicles
 */
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    
    // ENCAPSULATION: Private fields with controlled access
    @Id
    private String licenseNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", insertable = false, updatable = false)
    private VehicleType vehicleType;
    
    private String color;
    private String model;
    
    /**
     * Default constructor for JPA
     */
    protected Vehicle() {}
    
    /**
     * Protected constructor ensures only subclasses can create instances
     * This enforces the use of specific vehicle types
     */
    protected Vehicle(String licenseNumber, VehicleType vehicleType, String color, String model) {
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be null or empty");
        }
        this.licenseNumber = licenseNumber.toUpperCase().trim();
        this.vehicleType = vehicleType;
        this.color = color;
        this.model = model;
    }
    
    /**
     * ABSTRACT METHOD: Subclasses must implement their specific parking requirements
     * This enforces POLYMORPHISM - each vehicle type can have different requirements
     */
    public abstract int getParkingSpaceRequired();
    
    /**
     * ABSTRACT METHOD: Each vehicle type may have different display formats
     */
    public abstract String getDisplayInfo();
    
    /**
     * ENCAPSULATION: Getter methods
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }
    
    public VehicleType getVehicleType() {
        return vehicleType;
    }
    
    public String getColor() {
        return color;
    }
    
    public String getModel() {
        return model;
    }
    
    /**
     * TEMPLATE METHOD PATTERN: Common validation logic for all vehicles
     */
    public boolean isValidLicenseNumber() {
        return licenseNumber != null && 
               licenseNumber.matches("^[A-Z0-9]{6,10}$"); // Basic pattern
    }
    
    /**
     * POLYMORPHISM: Override toString for better object representation
     */
    @Override
    public String toString() {
        return String.format("%s [%s] - %s %s", 
            vehicleType.getDisplayName(), licenseNumber, color, model);
    }
}
