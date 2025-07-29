package com.lld.parkinglot.models;





import java.time.LocalDateTime;

/**
 * NOTIFICATION EVENT MODEL - Used with OBSERVER PATTERN
 * 
 * This class represents events that trigger notifications.
 * It's used by the Observer pattern to notify different systems.
 * 
 * DESIGN PRINCIPLES:
 * 1. IMMUTABILITY: Events shouldn't change after creation
 * 2. VALUE OBJECT: Contains event data and context
 * 3. SINGLE RESPONSIBILITY: Only represents notification events
 */


public class NotificationEvent {
    
    public enum EventType {
        VEHICLE_ENTRY("Vehicle Entered"),
        VEHICLE_EXIT("Vehicle Exited"),
        PAYMENT_COMPLETED("Payment Completed"),
        PAYMENT_FAILED("Payment Failed"),
        SPOT_RESERVED("Spot Reserved"),
        RESERVATION_EXPIRED("Reservation Expired"),
        LOT_FULL("Parking Lot Full"),
        SPOT_AVAILABLE("Spot Available"),
        MAINTENANCE_REQUIRED("Maintenance Required");
        
        private final String description;
        
        EventType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    private final EventType eventType;
    private final String message;
    private final LocalDateTime timestamp;
    private final Object eventData; // Can be ParkingTicket, Payment, etc.
    private final String recipientInfo; // Phone number, email, etc.
    
    /**
     * Constructor for creating notification events
     * IMMUTABILITY: All fields are final and set during construction
     */
    public NotificationEvent(EventType eventType, String message, Object eventData, String recipientInfo) {
        this.eventType = eventType;
        this.message = message;
        this.eventData = eventData;
        this.recipientInfo = recipientInfo;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * BUSINESS LOGIC: Check if event is urgent
     * Some events require immediate notification
     */
    public boolean isUrgent() {
        return eventType == EventType.PAYMENT_FAILED ||
               eventType == EventType.LOT_FULL ||
               eventType == EventType.MAINTENANCE_REQUIRED;
    }
    
    /**
     * BUSINESS LOGIC: Check if event should send SMS
     */
    public boolean shouldSendSMS() {
        return eventType == EventType.VEHICLE_ENTRY ||
               eventType == EventType.PAYMENT_COMPLETED ||
               eventType == EventType.PAYMENT_FAILED;
    }
    
    /**
     * BUSINESS LOGIC: Check if event should send email
     */
    public boolean shouldSendEmail() {
        return eventType == EventType.PAYMENT_COMPLETED ||
               eventType == EventType.RESERVATION_EXPIRED ||
               eventType == EventType.MAINTENANCE_REQUIRED;
    }
    
    /**
     * UTILITY: Get formatted event message
     */
    public String getFormattedMessage() {
        return String.format("[%s] %s - %s", 
            eventType.getDescription(), 
            timestamp.toString(), 
            message);
    }
    
    /**
     * UTILITY: Get event priority level
     */
    public int getPriorityLevel() {
        return switch (eventType) {
            case PAYMENT_FAILED, MAINTENANCE_REQUIRED -> 1; // High priority
            case LOT_FULL, VEHICLE_EXIT -> 2; // Medium priority
            default -> 3; // Low priority
        };
    }
    
    // ENCAPSULATION: Getter methods
    public EventType getEventType() { return eventType; }
    public String getMessage() { return message; }
    public Object getEventData() { return eventData; }
    public String getRecipientInfo() { return recipientInfo; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
