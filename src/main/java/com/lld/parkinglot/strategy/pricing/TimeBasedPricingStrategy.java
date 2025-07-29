package com.lld.parkinglot.strategy.pricing;

import com.lld.parkinglot.interfaces.PricingStrategy;
import com.lld.parkinglot.models.ParkingTicket;
import com.lld.parkinglot.enums.VehicleType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * STRATEGY PATTERN Implementation - Time-based Pricing
 * 
 * This pricing strategy calculates charges based on:
 * 1. Vehicle type (different rates)
 * 2. Time duration (hourly billing)
 * 3. Special discounts (early bird, loyalty, etc.)
 * 
 * SINGLE RESPONSIBILITY: Only handles pricing calculations
 */
@Component
public class TimeBasedPricingStrategy implements PricingStrategy {
    
    // CONSTANTS: Configuration values
    private static final double MOTORCYCLE_HOURLY_RATE = 2.0;
    private static final double CAR_HOURLY_RATE = 5.0;
    private static final double TRUCK_HOURLY_RATE = 10.0;
    
    // Discount thresholds
    private static final int EARLY_BIRD_START_HOUR = 6;
    private static final int EARLY_BIRD_END_HOUR = 10;
    private static final double EARLY_BIRD_DISCOUNT = 0.20; // 20% discount
    
    private static final int LONG_TERM_THRESHOLD_HOURS = 24;
    private static final double LONG_TERM_DISCOUNT = 0.15; // 15% discount
    
    @Override
    public double calculateParkingCharges(ParkingTicket ticket) {
        if (ticket == null) {
            return 0.0;
        }
        
        // Get parking duration in hours (minimum 1 hour billing)
        long durationHours = Math.max(1, ticket.getParkingDurationHours());
        
        // Get base rate for vehicle type
        double hourlyRate = getHourlyRate(ticket);
        
        // Calculate base charges
        double baseCharges = durationHours * hourlyRate;
        
        // Apply discounts
        double discount = calculateDiscount(ticket);
        
        // Calculate final amount
        double finalAmount = baseCharges - discount;
        
        // Ensure minimum charge
        return Math.max(finalAmount, getMinimumCharge(ticket.getVehicle().getVehicleType()));
    }
    
    @Override
    public double getHourlyRate(ParkingTicket ticket) {
        VehicleType vehicleType = ticket.getVehicle().getVehicleType();
        
        return switch (vehicleType) {
            case MOTORCYCLE -> MOTORCYCLE_HOURLY_RATE;
            case CAR -> CAR_HOURLY_RATE;
            case TRUCK -> TRUCK_HOURLY_RATE;
        };
    }
    
    @Override
    public double calculateDiscount(ParkingTicket ticket) {
        double totalDiscount = 0.0;
        
        // Calculate base charges for discount calculation
        long durationHours = Math.max(1, ticket.getParkingDurationHours());
        double hourlyRate = getHourlyRate(ticket);
        double baseCharges = durationHours * hourlyRate;
        
        // Early bird discount (6 AM - 10 AM entry)
        if (isEarlyBirdEntry(ticket.getEntryTime())) {
            totalDiscount += baseCharges * EARLY_BIRD_DISCOUNT;
        }
        
        // Long-term parking discount (>24 hours)
        if (durationHours >= LONG_TERM_THRESHOLD_HOURS) {
            totalDiscount += baseCharges * LONG_TERM_DISCOUNT;
        }
        
        // Weekend discount (different rates could apply)
        if (isWeekend(ticket.getEntryTime())) {
            totalDiscount += calculateWeekendDiscount(baseCharges);
        }
        
        return totalDiscount;
    }
    
    /**
     * BUSINESS LOGIC: Check if entry qualifies for early bird discount
     */
    private boolean isEarlyBirdEntry(LocalDateTime entryTime) {
        int hour = entryTime.getHour();
        return hour >= EARLY_BIRD_START_HOUR && hour < EARLY_BIRD_END_HOUR;
    }
    
    /**
     * BUSINESS LOGIC: Check if it's weekend
     */
    private boolean isWeekend(LocalDateTime dateTime) {
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7; // Saturday or Sunday
    }
    
    /**
     * BUSINESS LOGIC: Calculate weekend-specific discounts
     */
    private double calculateWeekendDiscount(double baseCharges) {
        // 10% discount on weekends for cars and motorcycles
        return baseCharges * 0.10;
    }
    
    /**
     * BUSINESS LOGIC: Get minimum charge based on vehicle type
     */
    private double getMinimumCharge(VehicleType vehicleType) {
        return switch (vehicleType) {
            case MOTORCYCLE -> 1.0;
            case CAR -> 2.0;
            case TRUCK -> 5.0;
        };
    }
    
    /**
     * UTILITY: Get pricing information for display
     */
    public String getPricingInfo() {
        return String.format(
            "💰 Pricing Information:\n" +
            "🏍️ Motorcycle: $%.2f/hour\n" +
            "🚗 Car: $%.2f/hour\n" +
            "🚛 Truck: $%.2f/hour\n" +
            "🌅 Early Bird Discount (6-10 AM): %.0f%%\n" +
            "📅 Long-term Parking (>24h): %.0f%%\n" +
            "📆 Weekend Discount: 10%%",
            MOTORCYCLE_HOURLY_RATE,
            CAR_HOURLY_RATE,
            TRUCK_HOURLY_RATE,
            EARLY_BIRD_DISCOUNT * 100,
            LONG_TERM_DISCOUNT * 100
        );
    }
}
