package com.lld.parkinglot.strategy.payment;

import com.lld.parkinglot.interfaces.PaymentStrategy;
import com.lld.parkinglot.enums.PaymentMethod;
import com.lld.parkinglot.models.ParkingTicket;
import com.lld.parkinglot.models.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.Random;

/**
 * STRATEGY PATTERN Implementation - Digital Wallet Payment
 * 
 * This class handles digital wallet payments (like PayPal, Google Pay, etc.)
 */
@Component
public class DigitalWalletPaymentStrategy implements PaymentStrategy {
    
    private static final double PROCESSING_FEE_FLAT = 0.50; // Flat $0.50 fee
    private static final Random random = new Random();
    
    @Override
    public Payment processPayment(ParkingTicket ticket, double amount) {
        String transactionId = "DW_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        double processingFee = calculateAdditionalFees(amount);
        
        Payment payment = new Payment(transactionId, ticket, amount, PaymentMethod.DIGITAL_WALLET, processingFee);
        
        // Simulate digital wallet processing
        boolean paymentSuccessful = simulateDigitalWalletProcessing(amount);
        
        if (paymentSuccessful) {
            String walletReference = "WALLET_" + System.currentTimeMillis();
            payment.markAsSuccessful(walletReference);
        } else {
            payment.markAsFailed("Digital wallet transaction failed");
        }
        
        return payment;
    }
    
    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.DIGITAL_WALLET;
    }
    
    @Override
    public boolean canProcessPayment(double amount) {
        // Digital wallets typically have lower limits than credit cards
        return amount >= 0.50 && amount <= 5000.0;
    }
    
    @Override
    public double calculateAdditionalFees(double baseAmount) {
        // Flat fee for digital wallet transactions
        return PROCESSING_FEE_FLAT;
    }
    
    /**
     * SIMULATION: Digital wallet payment processing
     */
    private boolean simulateDigitalWalletProcessing(double amount) {
        // Simulate faster processing than credit cards
        try {
            Thread.sleep(500 + random.nextInt(1000)); // 0.5-1.5 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Simulate 98% success rate (digital wallets are typically more reliable)
        return random.nextDouble() < 0.98;
    }
}
