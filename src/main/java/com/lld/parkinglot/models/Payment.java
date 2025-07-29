package com.lld.parkinglot.models;

import com.lld.parkinglot.enums.PaymentMethod;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * PAYMENT MODEL - Demonstrates VALUE OBJECT pattern
 * 
 * This class encapsulates payment transaction details.
 * It's immutable once created (except for status updates).
 * 
 * DESIGN PRINCIPLES:
 * 1. IMMUTABILITY: Core payment details don't change after creation
 * 2. ENCAPSULATION: Private fields with controlled access
 * 3. SINGLE RESPONSIBILITY: Only handles payment data and validation
 */
@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String transactionId;
    
    @ManyToOne
    @JoinColumn(name = "parking_ticket_id", nullable = false)
    private ParkingTicket parkingTicket;
    
    @Column(nullable = false)
    private Double amount;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(nullable = false)
    private LocalDateTime paymentTime;
    
    private String paymentStatus; // SUCCESS, FAILED, PENDING
    
    private String paymentReference; // External payment gateway reference
    
    private Double processingFee;
    
    /**
     * Default constructor for JPA
     */
    public Payment() {}
    
    /**
     * Constructor for creating new payments
     * Demonstrates controlled object creation
     */
    public Payment(String transactionId, ParkingTicket parkingTicket, Double amount, 
                   PaymentMethod paymentMethod, Double processingFee) {
        this.transactionId = transactionId;
        this.parkingTicket = parkingTicket;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.processingFee = processingFee;
        this.paymentTime = LocalDateTime.now();
        this.paymentStatus = "PENDING";
    }
    
    /**
     * BUSINESS LOGIC: Calculate total amount including fees
     */
    public Double getTotalAmount() {
        return amount + (processingFee != null ? processingFee : 0.0);
    }
    
    /**
     * ENCAPSULATION: Controlled status update
     */
    public void markAsSuccessful(String paymentReference) {
        this.paymentStatus = "SUCCESS";
        this.paymentReference = paymentReference;
    }
    
    /**
     * ENCAPSULATION: Controlled status update
     */
    public void markAsFailed(String reason) {
        this.paymentStatus = "FAILED";
        this.paymentReference = reason;
    }
    
    /**
     * BUSINESS LOGIC: Check payment status
     */
    public boolean isSuccessful() {
        return "SUCCESS".equals(paymentStatus);
    }
    
    public boolean isFailed() {
        return "FAILED".equals(paymentStatus);
    }
    
    public boolean isPending() {
        return "PENDING".equals(paymentStatus);
    }
    
    /**
     * UTILITY: Get payment summary for display
     */
    public String getPaymentSummary() {
        return String.format(
            "💳 Payment %s\n" +
            "🎫 Ticket: %s\n" +
            "💰 Amount: $%.2f\n" +
            "💳 Method: %s\n" +
            "⏰ Time: %s\n" +
            "✅ Status: %s",
            transactionId,
            parkingTicket.getTicketNumber(),
            getTotalAmount(),
            paymentMethod.getDisplayName(),
            paymentTime,
            paymentStatus
        );
    }
    
    // ENCAPSULATION: Getter methods
    public Long getId() { return id; }
    public double getAmount() { return amount; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public LocalDateTime getPaymentTime() { return paymentTime; }
    public String getPaymentStatus() { return paymentStatus; }
    public String getTransactionId() { return transactionId; }
    public Double getProcessingFee() { return processingFee; }
    public ParkingTicket getParkingTicket() { return parkingTicket; }
    public String getPaymentReference() { return paymentReference; }
}
