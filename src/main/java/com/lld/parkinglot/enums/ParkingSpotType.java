package com.lld.parkinglot.enums;

/**
 * PARKING SPOT TYPE ENUM
 * 
 * Demonstrates OPEN/CLOSED PRINCIPLE:
 * - Closed for modification (existing types are stable)
 * - Open for extension (new parking types can be added easily)
 */
public enum ParkingSpotType {
    MOTORCYCLE_SPOT("Motorcycle Spot", VehicleType.MOTORCYCLE),
    CAR_SPOT("Car Spot", VehicleType.CAR),
    TRUCK_SPOT("Truck Spot", VehicleType.TRUCK),
    HANDICAPPED_SPOT("Handicapped Spot", VehicleType.CAR); // Special type for accessibility
    
    private final String displayName;
    private final VehicleType accommodates;
    
    ParkingSpotType(String displayName, VehicleType accommodates) {
        this.displayName = displayName;
        this.accommodates = accommodates;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public VehicleType getAccommodates() {
        return accommodates;
    }
    
    /**
     * BUSINESS LOGIC: Check if this spot type can accommodate a vehicle type
     * This demonstrates SINGLE RESPONSIBILITY - each method has one clear purpose
     */
    public boolean canAccommodate(VehicleType vehicleType) {
        // A larger spot can accommodate smaller vehicles
        // Truck spots can hold cars and motorcycles
        // Car spots can hold motorcycles
        // Motorcycle spots only hold motorcycles
        return switch (this) {
            case TRUCK_SPOT -> true; // Can accommodate all vehicles
            case CAR_SPOT, HANDICAPPED_SPOT -> 
                vehicleType == VehicleType.CAR || vehicleType == VehicleType.MOTORCYCLE;
            case MOTORCYCLE_SPOT -> vehicleType == VehicleType.MOTORCYCLE;
        };
    }
}
