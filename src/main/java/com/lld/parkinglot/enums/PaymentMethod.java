package com.lld.parkinglot.enums;

/**
 * PAYMENT METHOD ENUM
 * 
 * This enum will be used with the STRATEGY PATTERN for payment processing.
 * Each payment method will have its own strategy implementation.
 */
public enum PaymentMethod {
    CASH("Cash Payment"),
    CREDIT_CARD("Credit Card"),
    DEBIT_CARD("Debit Card"),
    DIGITAL_WALLET("Digital Wallet"),
    UPI("UPI Payment");
    
    private final String displayName;
    
    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

/**
 * PARKING STATUS ENUM
 * 
 * Represents the current state of a parking spot.
 * This is part of STATE PATTERN implementation.
 */
enum ParkingSpotStatus {
    AVAILABLE("Available for parking"),
    OCCUPIED("Currently occupied"),
    RESERVED("Reserved for specific user"),
    OUT_OF_SERVICE("Temporarily unavailable");
    
    private final String description;
    
    ParkingSpotStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}
