package com.lld.parkinglot.enums;

/**
 * VEHICLE TYPE ENUM
 * 
 * This enum demonstrates ENCAPSULATION and TYPE SAFETY.
 * Instead of using strings or integers, we use enums to:
 * 1. Prevent invalid values
 * 2. Make code more readable
 * 3. Enable compile-time checking
 * 4. Support switch statements efficiently
 */
public enum VehicleType {
    MOTORCYCLE("Motorcycle", 1),
    CAR("Car", 2), 
    TRUCK("Truck", 4);
    
    private final String displayName;
    private final int size; // Size units for parking calculation
    
    /**
     * ENCAPSULATION: Private constructor ensures controlled object creation
     */
    VehicleType(String displayName, int size) {
        this.displayName = displayName;
        this.size = size;
    }
    
    /**
     * ENCAPSULATION: Getter methods provide controlled access to private fields
     */
    public String getDisplayName() {
        return displayName;
    }
    
    public int getSize() {
        return size;
    }
    
    /**
     * POLYMORPHISM: Override toString for better representation
     */
    @Override
    public String toString() {
        return displayName;
    }
}
