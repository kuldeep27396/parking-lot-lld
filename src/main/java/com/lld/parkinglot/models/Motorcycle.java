package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.VehicleType;
import jakarta.persistence.*;

/**
 * CONCRETE VEHICLE CLASS - INHERITANCE Example
 * 
 * This class demonstrates:
 * 1. INHERITANCE: Extends Vehicle base class
 * 2. POLYMORPHISM: Provides specific implementation for abstract methods
 * 3. LISKOV SUBSTITUTION PRINCIPLE: Can be used wherever Vehicle is expected
 */
@Entity
@DiscriminatorValue("MOTORCYCLE")
public class Motorcycle extends Vehicle {
    
    private boolean hasSidecar;
    
    /**
     * Default constructor for JPA
     */
    public Motorcycle() {}
    
    /**
     * Constructor calls parent constructor (INHERITANCE)
     */
    public Motorcycle(String licenseNumber, String color, String model, boolean hasSidecar) {
        super(licenseNumber, VehicleType.MOTORCYCLE, color, model);
        this.hasSidecar = hasSidecar;
    }
    
    /**
     * POLYMORPHISM: Specific implementation for motorcycles
     * Motorcycles require minimal parking space
     */
    @Override
    public int getParkingSpaceRequired() {
        return hasSidecar ? 2 : 1; // Sidecar motorcycles need more space
    }
    
    /**
     * POLYMORPHISM: Motorcycle-specific display format
     */
    @Override
    public String getDisplayInfo() {
        return String.format("🏍️ Motorcycle: %s [%s] %s %s%s", 
            getLicenseNumber(), 
            getColor(), 
            getModel(),
            hasSidecar ? " (with sidecar)" : "",
            isValidLicenseNumber() ? " ✅" : " ❌");
    }
    
    /**
     * ADDITIONAL BEHAVIOR: Motorcycle-specific methods
     */
    public boolean isCompact() {
        return !hasSidecar;
    }
    
    public boolean hasSidecar() {
        return hasSidecar;
    }
}
