package com.lld.parkinglot.observer;

import com.lld.parkinglot.interfaces.NotificationObserver;
import com.lld.parkinglot.models.NotificationEvent;
import org.springframework.stereotype.Component;

/**
 * OBSERVER PATTERN Implementation - Email Notification
 * 
 * This observer handles email notifications for parking events.
 */
@Component
public class EmailNotificationObserver implements NotificationObserver {
    
    @Override
    public void handleNotification(NotificationEvent event) {
        if (!shouldHandle(event)) {
            return;
        }
        
        String subject = generateEmailSubject(event);
        String body = generateEmailBody(event);
        
        sendEmail(event.getRecipientInfo(), subject, body);
    }
    
    @Override
    public String getObserverType() {
        return "EMAIL_NOTIFICATION";
    }
    
    @Override
    public boolean shouldHandle(NotificationEvent event) {
        // Handle events that benefit from detailed email communication
        return event.shouldSendEmail() || 
               event.getEventType() == NotificationEvent.EventType.PAYMENT_COMPLETED;
    }
    
    /**
     * Generate appropriate email subject
     */
    private String generateEmailSubject(NotificationEvent event) {
        return switch (event.getEventType()) {
            case PAYMENT_COMPLETED -> "Parking Payment Confirmation";
            case RESERVATION_EXPIRED -> "Parking Reservation Expired";
            case MAINTENANCE_REQUIRED -> "Parking Spot Maintenance Notice";
            default -> "Parking Lot Notification - " + event.getEventType().getDescription();
        };
    }
    
    /**
     * Generate detailed email body
     */
    private String generateEmailBody(NotificationEvent event) {
        StringBuilder body = new StringBuilder();
        body.append("Dear Customer,\n\n");
        
        switch (event.getEventType()) {
            case PAYMENT_COMPLETED -> {
                body.append("Your parking payment has been processed successfully.\n\n");
                body.append("Transaction Details:\n");
                body.append(event.getMessage()).append("\n\n");
                body.append("Thank you for using our parking services!\n");
            }
            case RESERVATION_EXPIRED -> {
                body.append("Your parking spot reservation has expired.\n\n");
                body.append("Details: ").append(event.getMessage()).append("\n\n");
                body.append("Please make a new reservation if you still need parking.\n");
            }
            case MAINTENANCE_REQUIRED -> {
                body.append("Maintenance Notice:\n\n");
                body.append(event.getMessage()).append("\n\n");
                body.append("We apologize for any inconvenience.\n");
            }
            default -> {
                body.append(event.getFormattedMessage()).append("\n\n");
            }
        }
        
        body.append("Best regards,\n");
        body.append("Parking Management Team\n\n");
        body.append("This is an automated message. Please do not reply.");
        
        return body.toString();
    }
    
    /**
     * SIMULATION: Send email (in real implementation, integrate with email service)
     */
    private void sendEmail(String email, String subject, String body) {
        if (email == null || !isValidEmail(email)) {
            System.out.println("📧 Email: Invalid email address provided");
            return;
        }
        
        System.out.printf("📧 Email to %s%n", maskEmail(email));
        System.out.printf("Subject: %s%n", subject);
        System.out.println("Body: " + body.substring(0, Math.min(100, body.length())) + "...");
        System.out.println("--- Email sent successfully ---\n");
        
        // In real implementation:
        // - Integrate with email service (SendGrid, AWS SES, etc.)
        // - Handle HTML formatting
        // - Attachment support
        // - Delivery tracking
        // - Unsubscribe management
    }
    
    /**
     * VALIDATION: Basic email validation
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
    
    /**
     * UTILITY: Mask email for privacy in logs
     */
    private String maskEmail(String email) {
        if (!email.contains("@")) {
            return email;
        }
        
        String[] parts = email.split("@");
        String username = parts[0];
        String domain = parts[1];
        
        if (username.length() <= 2) {
            return email;
        }
        
        return username.substring(0, 2) + "***@" + domain;
    }
}
