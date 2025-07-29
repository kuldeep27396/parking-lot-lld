package com.lld.parkinglot.interfaces;

import com.lld.parkinglot.enums.PaymentMethod;
import com.lld.parkinglot.models.ParkingTicket;
import com.lld.parkinglot.models.Payment;

/**
 * STRATEGY PATTERN Interface
 * 
 * This interface enables different payment strategies.
 * Each payment method (Cash, Card, Digital Wallet) will implement this interface.
 * 
 * BENEFITS:
 * - Easy to add new payment methods without modifying existing code (Open/Closed Principle)
 * - Each payment strategy is independent and testable
 * - Runtime strategy selection possible
 */
public interface PaymentStrategy {
    
    /**
     * Process payment for a parking ticket
     * @param ticket The parking ticket containing amount details
     * @param amount The amount to be paid
     * @return Payment object with transaction details
     */
    Payment processPayment(ParkingTicket ticket, double amount);
    
    /**
     * Get the payment method type
     * @return PaymentMethod enum value
     */
    PaymentMethod getPaymentMethod();
    
    /**
     * Validate if payment can be processed
     * @param amount The amount to validate
     * @return true if payment can be processed
     */
    boolean canProcessPayment(double amount);
    
    /**
     * Calculate any additional fees for this payment method
     * @param baseAmount The base parking amount
     * @return Additional fees (e.g., processing charges)
     */
    double calculateAdditionalFees(double baseAmount);
}
