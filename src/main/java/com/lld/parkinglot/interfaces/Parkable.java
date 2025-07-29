package com.lld.parkinglot.interfaces;

import com.lld.parkinglot.models.Vehicle;
import com.lld.parkinglot.models.ParkingTicket;

/**
 * INTERFACE SEGREGATION PRINCIPLE (ISP) Example
 * 
 * This interface defines only parking-related operations.
 * We separate parking operations from payment, notification, etc.
 * 
 * WHY ISP?
 * - Not all classes need all parking operations
 * - Easier to implement and test
 * - Reduces coupling between components
 * - Follows Single Responsibility Principle
 */
public interface Parkable {
    
    /**
     * Park a vehicle and return a parking ticket
     * @param vehicle The vehicle to park
     * @return ParkingTicket if successful, null if parking failed
     */
    ParkingTicket parkVehicle(Vehicle vehicle);
    
    /**
     * Remove a vehicle from parking
     * @param ticket The parking ticket
     * @return true if successful, false otherwise
     */
    boolean unparkVehicle(ParkingTicket ticket);
    
    /**
     * Check if parking is available for a vehicle type
     * @param vehicle The vehicle to check availability for
     * @return true if parking is available
     */
    boolean isSpaceAvailable(Vehicle vehicle);
}
