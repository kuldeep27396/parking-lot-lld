package com.lld.parkinglot.observer;

import com.lld.parkinglot.interfaces.NotificationObserver;
import com.lld.parkinglot.models.NotificationEvent;
import org.springframework.stereotype.Component;

/**
 * OBSERVER PATTERN Implementation - SMS Notification
 * 
 * This observer handles SMS notifications for parking events.
 * 
 * BENEFITS:
 * 1. SINGLE RESPONSIBILITY: Only handles SMS notifications
 * 2. LOOSE COUPLING: Doesn't depend on parking lot implementation
 * 3. OBSERVER PATTERN: Can be added/removed dynamically
 */
@Component
public class SMSNotificationObserver implements NotificationObserver {
    
    @Override
    public void handleNotification(NotificationEvent event) {
        if (!shouldHandle(event)) {
            return;
        }
        
        String message = formatSMSMessage(event);
        sendSMS(event.getRecipientInfo(), message);
    }
    
    @Override
    public String getObserverType() {
        return "SMS_NOTIFICATION";
    }
    
    @Override
    public boolean shouldHandle(NotificationEvent event) {
        // Handle urgent events and events that require immediate attention
        return event.shouldSendSMS() || event.isUrgent();
    }
    
    /**
     * FORMAT: Create SMS-appropriate message
     */
    private String formatSMSMessage(NotificationEvent event) {
        return switch (event.getEventType()) {
            case VEHICLE_ENTRY -> 
                "🚗 Vehicle parked successfully. " + event.getMessage();
            case VEHICLE_EXIT -> 
                "🚪 Vehicle exit recorded. " + event.getMessage();
            case PAYMENT_COMPLETED -> 
                "💳 Payment successful. Thank you for using our parking!";
            case PAYMENT_FAILED -> 
                "❌ Payment failed. Please try again or contact support.";
            case LOT_FULL -> 
                "🚫 Parking lot is full. Please try alternative locations.";
            default -> 
                event.getFormattedMessage();
        };
    }
    
    /**
     * SIMULATION: Send SMS (in real implementation, integrate with SMS gateway)
     */
    private void sendSMS(String phoneNumber, String message) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            System.out.println("📱 SMS: No phone number provided for notification");
            return;
        }
        
        System.out.printf("📱 SMS to %s: %s%n", 
            maskPhoneNumber(phoneNumber), message);
        
        // In real implementation:
        // - Integrate with SMS gateway (Twilio, AWS SNS, etc.)
        // - Handle delivery status
        // - Retry logic for failed messages
        // - Rate limiting and cost management
    }
    
    /**
     * UTILITY: Mask phone number for privacy in logs
     */
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() <= 4) {
            return phoneNumber;
        }
        
        return phoneNumber.substring(0, 3) + "***" + 
               phoneNumber.substring(phoneNumber.length() - 2);
    }
}
