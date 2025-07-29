package com.lld.parkinglot.strategy.payment;

import com.lld.parkinglot.interfaces.PaymentStrategy;
import com.lld.parkinglot.enums.PaymentMethod;
import com.lld.parkinglot.models.ParkingTicket;
import com.lld.parkinglot.models.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.Random;

/**
 * STRATEGY PATTERN Implementation - Credit Card Payment
 * 
 * This class handles credit card payment processing with validation
 * and gateway simulation.
 */
@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {
    
    private static final double PROCESSING_FEE_PERCENTAGE = 0.025; // 2.5% processing fee
    private static final Random random = new Random();
    
    @Override
    public Payment processPayment(ParkingTicket ticket, double amount) {
        String transactionId = "CC_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        double processingFee = calculateAdditionalFees(amount);
        
        Payment payment = new Payment(transactionId, ticket, amount, PaymentMethod.CREDIT_CARD, processingFee);
        
        // Simulate credit card processing
        boolean paymentSuccessful = simulateCreditCardProcessing(amount);
        
        if (paymentSuccessful) {
            String gatewayReference = "GW_" + System.currentTimeMillis();
            payment.markAsSuccessful(gatewayReference);
        } else {
            payment.markAsFailed("Card declined or insufficient funds");
        }
        
        return payment;
    }
    
    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.CREDIT_CARD;
    }
    
    @Override
    public boolean canProcessPayment(double amount) {
        // Credit cards typically have minimum and maximum limits
        return amount >= 1.0 && amount <= 10000.0;
    }
    
    @Override
    public double calculateAdditionalFees(double baseAmount) {
        return baseAmount * PROCESSING_FEE_PERCENTAGE;
    }
    
    /**
     * SIMULATION: Credit card payment gateway
     * In real implementation, this would call external payment gateway
     */
    private boolean simulateCreditCardProcessing(double amount) {
        // Simulate processing delay
        try {
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate 95% success rate
        return random.nextDouble() < 0.95;
    }
}
