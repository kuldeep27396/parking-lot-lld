package com.lld.parkinglot.interfaces;

import com.lld.parkinglot.models.ParkingTicket;

/**
 * SINGLE RESPONSIBILITY PRINCIPLE Interface
 * 
 * This interface is responsible ONLY for pricing calculations.
 * It's separate from parking operations, payment processing, etc.
 * 
 * BENEFITS:
 * - Easy to modify pricing logic without affecting other components
 * - Multiple pricing strategies can be implemented
 * - Testable in isolation
 */
public interface PricingStrategy {
    
    /**
     * Calculate parking charges based on parking duration
     * @param ticket The parking ticket with entry time
     * @return Total parking charges
     */
    double calculateParkingCharges(ParkingTicket ticket);
    
    /**
     * Get hourly rate for a specific vehicle type
     * @param ticket The parking ticket
     * @return Hourly parking rate
     */
    double getHourlyRate(ParkingTicket ticket);
    
    /**
     * Calculate any applicable discounts
     * @param ticket The parking ticket
     * @return Discount amount
     */
    double calculateDiscount(ParkingTicket ticket);
}
