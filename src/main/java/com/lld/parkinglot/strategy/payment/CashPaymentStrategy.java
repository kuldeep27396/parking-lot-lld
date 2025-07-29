package com.lld.parkinglot.strategy.payment;

import com.lld.parkinglot.interfaces.PaymentStrategy;
import com.lld.parkinglot.enums.PaymentMethod;
import com.lld.parkinglot.models.ParkingTicket;
import com.lld.parkinglot.models.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * STRATEGY PATTERN Implementation - Cash Payment
 * 
 * This class implements cash payment strategy.
 * Each payment method has its own strategy implementation.
 * 
 * BENEFITS:
 * 1. SINGLE RESPONSIBILITY: Only handles cash payment logic
 * 2. OPEN/CLOSED PRINCIPLE: New payment methods can be added without modifying existing ones
 * 3. STRATEGY PATTERN: Runtime algorithm selection
 */
@Component
public class CashPaymentStrategy implements PaymentStrategy {
    
    /**
     * STRATEGY METHOD: Process cash payment
     * Cash payments are always successful (assuming correct amount)
     */
    @Override
    public Payment processPayment(ParkingTicket ticket, double amount) {
        // Generate transaction ID
        String transactionId = "CASH_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Calculate processing fee (cash has no processing fee)
        double processingFee = calculateAdditionalFees(amount);
        
        // Create payment object
        Payment payment = new Payment(transactionId, ticket, amount, PaymentMethod.CASH, processingFee);
        
        // Cash payments are typically successful immediately
        // In real world, this might involve cash counting verification
        payment.markAsSuccessful("CASH_RECEIVED");
        
        return payment;
    }
    
    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CASH;
    }
    
    /**
     * VALIDATION: Cash payments can always be processed if amount is positive
     */
    @Override
    public boolean canProcessPayment(double amount) {
        return amount > 0;
    }
    
    /**
     * BUSINESS LOGIC: Cash payments have no additional fees
     */
    @Override
    public double calculateAdditionalFees(double baseAmount) {
        return 0.0; // No processing fees for cash
    }
}
