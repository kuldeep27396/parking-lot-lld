package com.lld.parkinglot.interfaces;

import com.lld.parkinglot.models.NotificationEvent;

/**
 * OBSERVER PATTERN Interface
 * 
 * This interface defines observers that can be notified of parking lot events.
 * Examples: Email notifications, SMS alerts, mobile app notifications
 * 
 * OBSERVER PATTERN BENEFITS:
 * - Loose coupling between subject (parking lot) and observers (notification services)
 * - Dynamic subscription/unsubscription of observers
 * - Broadcasting events to multiple observers simultaneously
 */
public interface NotificationObserver {
    
    /**
     * Handle notification events
     * @param event The notification event containing details
     */
    void handleNotification(NotificationEvent event);
    
    /**
     * Get the observer type/name for identification
     * @return Observer identifier
     */
    String getObserverType();
    
    /**
     * Check if this observer should handle this type of event
     * @param event The notification event
     * @return true if this observer should handle the event
     */
    boolean shouldHandle(NotificationEvent event);
}
