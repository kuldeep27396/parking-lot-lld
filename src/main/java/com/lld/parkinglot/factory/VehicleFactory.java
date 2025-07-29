package com.lld.parkinglot.factory;

import com.lld.parkinglot.models.Vehicle;
import com.lld.parkinglot.models.Motorcycle;
import com.lld.parkinglot.models.Car;
import com.lld.parkinglot.models.Truck;
import com.lld.parkinglot.enums.VehicleType;
import org.springframework.stereotype.Component;

/**
 * FACTORY PATTERN Implementation
 * 
 * This factory creates different types of vehicles without exposing
 * the instantiation logic to the client.
 * 
 * BENEFITS:
 * 1. OPEN/CLOSED PRINCIPLE: Open for extension (new vehicle types), 
 *    closed for modification (existing code doesn't change)
 * 2. SINGLE RESPONSIBILITY: Only responsible for creating vehicles
 * 3. ENCAPSULATION: Hides complex object creation logic
 * 4. LOOSE COUPLING: Client doesn't depend on concrete classes
 */
@Component
public class VehicleFactory {
    
    /**
     * Create a vehicle based on type and parameters
     * 
     * This method demonstrates POLYMORPHISM - it returns Vehicle interface
     * but actual implementation depends on the type parameter.
     */
    public Vehicle createVehicle(VehicleType type, String licenseNumber, String color, String model, Object... additionalParams) {
        
        // VALIDATION: Ensure required parameters are provided
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number cannot be null or empty");
        }
        
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        
        // FACTORY METHOD PATTERN: Create appropriate vehicle type
        return switch (type) {
            case MOTORCYCLE -> createMotorcycle(licenseNumber, color, model, additionalParams);
            case CAR -> createCar(licenseNumber, color, model, additionalParams);
            case TRUCK -> createTruck(licenseNumber, color, model, additionalParams);
        };
    }
    
    /**
     * PRIVATE FACTORY METHODS: Encapsulate specific creation logic
     * These methods handle the complexity of creating each vehicle type
     */
    private Motorcycle createMotorcycle(String licenseNumber, String color, String model, Object... params) {
        // Default values for optional parameters
        boolean hasSidecar = false;
        
        // Parse additional parameters if provided
        if (params.length > 0 && params[0] instanceof Boolean) {
            hasSidecar = (Boolean) params[0];
        }
        
        return new Motorcycle(licenseNumber, color, model, hasSidecar);
    }
    
    private Car createCar(String licenseNumber, String color, String model, Object... params) {
        // Default values
        int numberOfDoors = 4;
        boolean isElectric = false;
        
        // Parse additional parameters
        if (params.length > 0 && params[0] instanceof Integer) {
            numberOfDoors = (Integer) params[0];
        }
        if (params.length > 1 && params[1] instanceof Boolean) {
            isElectric = (Boolean) params[1];
        }
        
        return new Car(licenseNumber, color, model, numberOfDoors, isElectric);
    }
    
    private Truck createTruck(String licenseNumber, String color, String model, Object... params) {
        // Default values
        double cargoCapacity = 5.0; // tons
        int numberOfAxles = 2;
        
        // Parse additional parameters
        if (params.length > 0 && params[0] instanceof Double) {
            cargoCapacity = (Double) params[0];
        }
        if (params.length > 1 && params[1] instanceof Integer) {
            numberOfAxles = (Integer) params[1];
        }
        
        return new Truck(licenseNumber, color, model, cargoCapacity, numberOfAxles);
    }
    
    /**
     * CONVENIENCE METHODS: Simplified creation for common scenarios
     * These methods demonstrate METHOD OVERLOADING
     */
    public Motorcycle createMotorcycle(String licenseNumber, String color, String model) {
        return createMotorcycle(licenseNumber, color, model, false);
    }
    
    public Car createCar(String licenseNumber, String color, String model) {
        return createCar(licenseNumber, color, model, 4, false);
    }
    
    public Car createElectricCar(String licenseNumber, String color, String model) {
        return createCar(licenseNumber, color, model, 4, true);
    }
    
    public Truck createTruck(String licenseNumber, String color, String model) {
        return createTruck(licenseNumber, color, model, 5.0, 2);
    }
    
    /**
     * VALIDATION METHOD: Check if vehicle creation parameters are valid
     * This demonstrates SINGLE RESPONSIBILITY - validation is separate from creation
     */
    public boolean isValidVehicleData(VehicleType type, String licenseNumber, String color, String model) {
        if (type == null || licenseNumber == null || color == null || model == null) {
            return false;
        }
        
        if (licenseNumber.trim().isEmpty() || color.trim().isEmpty() || model.trim().isEmpty()) {
            return false;
        }
        
        // Additional validation can be added here
        return licenseNumber.matches("^[A-Z0-9]{6,10}$");
    }
}
