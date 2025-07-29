package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.VehicleType;
import jakarta.persistence.*;

/**
 * TRUCK CLASS - INHERITANCE Example
 * 
 * Demonstrates specialized vehicle behavior for larger vehicles.
 */
@Entity
@DiscriminatorValue("TRUCK")
public class Truck extends Vehicle {
    
    private double cargoCapacity; // in tons
    private int numberOfAxles;
    
    /**
     * Default constructor for JPA
     */
    public Truck() {}
    
    public Truck(String licenseNumber, String color, String model, double cargoCapacity, int numberOfAxles) {
        super(licenseNumber, VehicleType.TRUCK, color, model);
        this.cargoCapacity = cargoCapacity;
        this.numberOfAxles = numberOfAxles;
    }
    
    /**
     * POLYMORPHISM: Trucks require more parking space
     */
    @Override
    public int getParkingSpaceRequired() {
        // Larger trucks need more space
        if (cargoCapacity > 10) return 4;
        if (cargoCapacity > 5) return 3;
        return 2;
    }
    
    /**
     * POLYMORPHISM: Truck-specific display format
     */
    @Override
    public String getDisplayInfo() {
        return String.format("🚛 Truck: %s [%s] %s (%.1ft, %d axles)%s", 
            getLicenseNumber(), 
            getColor(), 
            getModel(),
            cargoCapacity,
            numberOfAxles,
            isValidLicenseNumber() ? " ✅" : " ❌");
    }
    
    /**
     * ADDITIONAL BEHAVIOR: Truck-specific methods
     */
    public boolean isHeavyVehicle() {
        return cargoCapacity > 7.5; // Heavy vehicle threshold
    }
    
    public boolean requiresSpecialParking() {
        return cargoCapacity > 15 || numberOfAxles > 4;
    }
    
    public double getCargoCapacity() {
        return cargoCapacity;
    }
    
    public int getNumberOfAxles() {
        return numberOfAxles;
    }
}
