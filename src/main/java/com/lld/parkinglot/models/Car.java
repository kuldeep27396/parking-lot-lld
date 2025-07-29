package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.VehicleType;
import jakarta.persistence.*;

/**
 * CAR CLASS - INHERITANCE and POLYMORPHISM Example
 * 
 * Demonstrates how different vehicle types can have different behaviors
 * while maintaining the same interface.
 */
@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle {
    
    private int numberOfDoors;
    private boolean isElectric;
    
    /**
     * Default constructor for JPA
     */
    public Car() {}
    
    public Car(String licenseNumber, String color, String model, int numberOfDoors, boolean isElectric) {
        super(licenseNumber, VehicleType.CAR, color, model);
        this.numberOfDoors = numberOfDoors;
        this.isElectric = isElectric;
    }
    
    /**
     * POLYMORPHISM: Car-specific space requirements
     */
    @Override
    public int getParkingSpaceRequired() {
        return isElectric ? 2 : 1; // Electric cars might need charging station space
    }
    
    /**
     * POLYMORPHISM: Car-specific display format
     */
    @Override
    public String getDisplayInfo() {
        return String.format("🚗 Car: %s [%s] %s (%d doors)%s%s", 
            getLicenseNumber(), 
            getColor(), 
            getModel(),
            numberOfDoors,
            isElectric ? " ⚡" : "",
            isValidLicenseNumber() ? " ✅" : " ❌");
    }
    
    /**
     * ADDITIONAL BEHAVIOR: Car-specific methods
     */
    public boolean needsChargingStation() {
        return isElectric;
    }
    
    public boolean isCompactCar() {
        return numberOfDoors <= 2;
    }
    
    public int getNumberOfDoors() {
        return numberOfDoors;
    }
    
    public boolean isElectric() {
        return isElectric;
    }
}
