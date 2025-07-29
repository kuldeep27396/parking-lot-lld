package com.lld.parkinglot.service;

import com.lld.parkinglot.interfaces.*;
import com.lld.parkinglot.models.*;
import com.lld.parkinglot.enums.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * PARKING LOT SERVICE - Demonstrates multiple design patterns
 * 
 * This service coordinates all parking lot operations and demonstrates:
 * 1. SINGLETON PATTERN (via Spring's @Service)
 * 2. FACADE PATTERN (provides simple interface to complex subsystem)
 * 3. OBSERVER PATTERN (notifies observers of events)
 * 4. STRATEGY PATTERN (uses different pricing and payment strategies)
 * 
 * SOLID PRINCIPLES:
 * 1. Single Responsibility: Manages parking lot operations
 * 2. Open/Closed: Open for extension via strategies
 * 3. Dependency Inversion: Depends on abstractions (interfaces)
 */
@Service
public class ParkingLotService implements Parkable {
    
    // DEPENDENCY INJECTION: Spring manages dependencies
    @Autowired
    private PricingStrategy pricingStrategy;
    
    @Autowired
    private List<NotificationObserver> notificationObservers;
    
    // ENCAPSULATION: Private fields with controlled access
    private final Map<String, ParkingSpot> parkingSpots;
    private final Map<String, ParkingTicket> activeTickets;
    private final AtomicLong ticketCounter;
    private final String parkingLotId;
    
    // Configuration
    private final int totalFloors;
    private final Map<ParkingSpotType, Integer> spotsPerFloor;
    
    public ParkingLotService() {
        this.parkingSpots = new ConcurrentHashMap<>();
        this.activeTickets = new ConcurrentHashMap<>();
        this.ticketCounter = new AtomicLong(1);
        this.parkingLotId = "PLT_" + UUID.randomUUID().toString().substring(0, 8);
        this.totalFloors = 5;
        this.spotsPerFloor = Map.of(
            ParkingSpotType.MOTORCYCLE_SPOT, 50,
            ParkingSpotType.CAR_SPOT, 100,
            ParkingSpotType.TRUCK_SPOT, 20,
            ParkingSpotType.HANDICAPPED_SPOT, 10
        );
        
        initializeParkingSpots();
    }
    
    /**
     * INITIALIZATION: Create parking spots for the lot
     * This method demonstrates FACTORY-like pattern for spot creation
     */
    private void initializeParkingSpots() {
        for (int floor = 1; floor <= totalFloors; floor++) {
            for (ParkingSpotType spotType : spotsPerFloor.keySet()) {
                int spotsOnFloor = spotsPerFloor.get(spotType);
                
                for (int spotNum = 1; spotNum <= spotsOnFloor; spotNum++) {
                    String spotNumber = String.format("%s-%d-%03d", 
                        spotType.name().substring(0, 1), floor, spotNum);
                    
                    ParkingSpot spot = new ParkingSpot(spotNumber, spotType, floor);
                    parkingSpots.put(spotNumber, spot);
                }
            }
        }
        
        System.out.printf("🏗️ Initialized parking lot with %d spots across %d floors%n", 
            parkingSpots.size(), totalFloors);
    }
    
    /**
     * FACADE PATTERN: Simple interface for complex parking operation
     * TEMPLATE METHOD: Standardized parking process
     */
    @Override
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        // VALIDATION
        if (vehicle == null || !vehicle.isValidLicenseNumber()) {
            throw new IllegalArgumentException("Invalid vehicle information");
        }
        
        // BUSINESS LOGIC: Find available spot
        ParkingSpot availableSpot = findAvailableSpot(vehicle);
        if (availableSpot == null) {
            notifyObservers(new NotificationEvent(
                NotificationEvent.EventType.LOT_FULL,
                "No available spots for vehicle type: " + vehicle.getVehicleType(),
                vehicle,
                null
            ));
            return null;
        }
        
        // ATOMIC OPERATION: Park vehicle and create ticket
        String ticketNumber = generateTicketNumber();
        ParkingTicket ticket = new ParkingTicket(ticketNumber, vehicle, availableSpot, null);
        
        // Update spot status
        availableSpot.parkVehicle(vehicle);
        
        // Store active ticket
        activeTickets.put(ticketNumber, ticket);
        
        // OBSERVER PATTERN: Notify observers of vehicle entry
        notifyObservers(new NotificationEvent(
            NotificationEvent.EventType.VEHICLE_ENTRY,
            "Vehicle parked successfully in spot " + availableSpot.getSpotNumber(),
            ticket,
            null
        ));
        
        return ticket;
    }
    
    /**
     * BUSINESS LOGIC: Find suitable parking spot for vehicle
     * Demonstrates ALGORITHM ENCAPSULATION
     */
    private ParkingSpot findAvailableSpot(Vehicle vehicle) {
        return parkingSpots.values().stream()
            .filter(spot -> spot.canAccommodate(vehicle))
            .filter(ParkingSpot::isAvailable)
            .min(Comparator.comparing(ParkingSpot::getFloorLevel)
                .thenComparing(ParkingSpot::getSpotNumber))
            .orElse(null);
    }
    
    @Override
    public boolean unparkVehicle(ParkingTicket ticket) {
        if (ticket == null || !activeTickets.containsKey(ticket.getTicketNumber())) {
            return false;
        }
        
        // Mark exit time
        ticket.markExit();
        
        // Free the parking spot
        ParkingSpot spot = ticket.getParkingSpot();
        spot.unparkVehicle();
        
        // Remove from active tickets
        activeTickets.remove(ticket.getTicketNumber());
        
        // OBSERVER PATTERN: Notify observers
        notifyObservers(new NotificationEvent(
            NotificationEvent.EventType.VEHICLE_EXIT,
            "Vehicle exited from spot " + spot.getSpotNumber(),
            ticket,
            null
        ));
        
        return true;
    }
    
    @Override
    public boolean isSpaceAvailable(Vehicle vehicle) {
        return findAvailableSpot(vehicle) != null;
    }
    
    /**
     * BUSINESS LOGIC: Calculate parking charges using strategy pattern
     */
    public double calculateParkingCharges(ParkingTicket ticket) {
        return pricingStrategy.calculateParkingCharges(ticket);
    }
    
    /**
     * OBSERVER PATTERN: Notify all registered observers
     */
    private void notifyObservers(NotificationEvent event) {
        if (notificationObservers != null) {
            notificationObservers.forEach(observer -> {
                if (observer.shouldHandle(event)) {
                    observer.handleNotification(event);
                }
            });
        }
    }
    
    /**
     * UTILITY: Generate unique ticket numbers
     */
    private String generateTicketNumber() {
        return String.format("TKT-%s-%06d", 
            parkingLotId.substring(4), 
            ticketCounter.getAndIncrement());
    }
    
    /**
     * QUERY METHODS: Provide information about parking lot state
     */
    public Map<ParkingSpotType, Long> getAvailableSpotCount() {
        return Arrays.stream(ParkingSpotType.values())
            .collect(HashMap::new,
                (map, type) -> map.put(type, 
                    parkingSpots.values().stream()
                        .filter(spot -> spot.getSpotType() == type)
                        .filter(ParkingSpot::isAvailable)
                        .count()),
                HashMap::putAll);
    }
    
    public double getOccupancyRate() {
        long totalSpots = parkingSpots.size();
        long occupiedSpots = parkingSpots.values().stream()
            .mapToLong(spot -> spot.isAvailable() ? 0 : 1)
            .sum();
        
        return totalSpots > 0 ? (double) occupiedSpots / totalSpots * 100 : 0;
    }
    
    public List<ParkingTicket> getActiveTickets() {
        return new ArrayList<>(activeTickets.values());
    }
    
    /**
     * ADMIN OPERATIONS: Management functionality
     */
    public boolean reserveSpot(String spotNumber, int durationMinutes) {
        ParkingSpot spot = parkingSpots.get(spotNumber);
        if (spot != null && spot.isAvailable()) {
            return spot.reserve(durationMinutes);
        }
        return false;
    }
    
    public void clearExpiredReservations() {
        parkingSpots.values().forEach(ParkingSpot::clearExpiredReservation);
    }
}
