package com.lld.parkinglot;

import com.lld.parkinglot.service.ParkingLotService;
import com.lld.parkinglot.factory.VehicleFactory;
import com.lld.parkinglot.models.*;
import com.lld.parkinglot.enums.*;
import com.lld.parkinglot.strategy.pricing.TimeBasedPricingStrategy;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * DEMONSTRATION CLASS - Shows all design patterns and principles in action
 * 
 * This class runs when the Spring Boot application starts and demonstrates
 * the complete parking lot system functionality.
 * 
 * LEARNING OBJECTIVES:
 * 1. See all design patterns working together
 * 2. Understand object interactions
 * 3. Observe SOLID principles in practice
 */
@Component
public class ParkingLotDemo implements CommandLineRunner {
    
    @Autowired
    private ParkingLotService parkingLotService;
    
    @Autowired
    private VehicleFactory vehicleFactory;
    
    @Autowired
    private TimeBasedPricingStrategy pricingStrategy;
    
    @Override
    public void run(String... args) {
        System.out.println("\n🚀 Starting Parking Lot System Demo");
        System.out.println("=".repeat(50));
        
        demonstrateSystemFeatures();
    }
    
    private void demonstrateSystemFeatures() {
        try {
            // 1. FACTORY PATTERN Demo
            demonstrateFactoryPattern();
            
            // 2. PARKING OPERATIONS Demo
            demonstrateParkingOperations();
            
            // 3. STRATEGY PATTERN Demo
            demonstrateStrategyPattern();
            
            // 4. OBSERVER PATTERN Demo
            demonstrateObserverPattern();
            
            // 5. SYSTEM STATUS Demo
            demonstrateSystemStatus();
            
        } catch (Exception e) {
            System.err.println("❌ Demo error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * FACTORY PATTERN Demonstration
     */
    private void demonstrateFactoryPattern() {
        System.out.println("\n🏭 FACTORY PATTERN Demonstration");
        System.out.println("-".repeat(40));
        
        // Create different vehicle types using factory
        Vehicle motorcycle = vehicleFactory.createVehicle(
            VehicleType.MOTORCYCLE, "BIKE123", "Red", "Honda CBR", false
        );
        
        Vehicle car = vehicleFactory.createVehicle(
            VehicleType.CAR, "CAR456", "Blue", "Toyota Camry", 4, true
        );
        
        Vehicle truck = vehicleFactory.createVehicle(
            VehicleType.TRUCK, "TRUCK789", "White", "Ford F-150", 8.5, 3
        );
        
        System.out.println("✅ Created vehicles using Factory Pattern:");
        System.out.println("   " + motorcycle.getDisplayInfo());
        System.out.println("   " + car.getDisplayInfo());
        System.out.println("   " + truck.getDisplayInfo());
    }
    
    /**
     * PARKING OPERATIONS Demonstration
     */
    private void demonstrateParkingOperations() {
        System.out.println("\n🅿️ PARKING OPERATIONS Demonstration");
        System.out.println("-".repeat(40));
        
        // Create vehicles
        Vehicle car1 = vehicleFactory.createCar("ABC123", "Red", "Honda Civic");
        Vehicle car2 = vehicleFactory.createElectricCar("EV456", "White", "Tesla Model 3");
        Vehicle motorcycle = vehicleFactory.createMotorcycle("BIKE789", "Black", "Harley Davidson");
        
        // Park vehicles
        System.out.println("🚗 Parking vehicles...");
        
        ParkingTicket ticket1 = parkingLotService.parkVehicle(car1);
        if (ticket1 != null) {
            System.out.println("✅ Parked: " + ticket1.getTicketSummary());
        }
        
        ParkingTicket ticket2 = parkingLotService.parkVehicle(car2);
        if (ticket2 != null) {
            System.out.println("✅ Parked: " + ticket2.getTicketSummary());
        }
        
        ParkingTicket ticket3 = parkingLotService.parkVehicle(motorcycle);
        if (ticket3 != null) {
            System.out.println("✅ Parked: " + ticket3.getTicketSummary());
        }
        
        // Simulate some time passing
        try {
            Thread.sleep(2000); // 2 seconds for demo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Exit first vehicle
        if (ticket1 != null) {
            System.out.println("\n🚪 Processing vehicle exit...");
            boolean exitSuccess = parkingLotService.unparkVehicle(ticket1);
            System.out.println("Exit status: " + (exitSuccess ? "✅ Success" : "❌ Failed"));
        }
    }
    
    /**
     * STRATEGY PATTERN Demonstration
     */
    private void demonstrateStrategyPattern() {
        System.out.println("\n💰 STRATEGY PATTERN Demonstration (Pricing)");
        System.out.println("-".repeat(40));
        
        // Show pricing information
        System.out.println(pricingStrategy.getPricingInfo());
        
        // Create test vehicles with different types
        Vehicle[] testVehicles = {
            vehicleFactory.createMotorcycle("TEST001", "Blue", "Yamaha"),
            vehicleFactory.createCar("TEST002", "Green", "BMW"),
            vehicleFactory.createTruck("TEST003", "Yellow", "Volvo")
        };
        
        System.out.println("\n📊 Sample Pricing Calculations:");
        
        for (Vehicle vehicle : testVehicles) {
            ParkingTicket ticket = parkingLotService.parkVehicle(vehicle);
            if (ticket != null) {
                // Simulate 3 hours of parking
                ticket.markExit(); // This would normally happen 3 hours later
                
                double charges = pricingStrategy.calculateParkingCharges(ticket);
                System.out.printf("   %s: $%.2f for %d hour(s)%n", 
                    vehicle.getVehicleType(), charges, ticket.getParkingDurationHours());
                
                parkingLotService.unparkVehicle(ticket);
            }
        }
    }
    
    /**
     * OBSERVER PATTERN Demonstration
     */
    private void demonstrateObserverPattern() {
        System.out.println("\n👀 OBSERVER PATTERN Demonstration (Notifications)");
        System.out.println("-".repeat(40));
        
        System.out.println("Observer pattern is automatically triggered when:");
        System.out.println("- Vehicles enter/exit the parking lot");
        System.out.println("- Payments are processed");
        System.out.println("- Special events occur (lot full, etc.)");
        
        // Park a vehicle to trigger notifications
        Vehicle demoVehicle = vehicleFactory.createCar("DEMO999", "Silver", "Demo Car");
        System.out.println("\n🔔 Triggering notifications by parking demo vehicle...");
        
        ParkingTicket demoTicket = parkingLotService.parkVehicle(demoVehicle);
        if (demoTicket != null) {
            System.out.println("   (Check console for SMS/Email notification simulations)");
            
            // Clean up
            parkingLotService.unparkVehicle(demoTicket);
        }
    }
    
    /**
     * SYSTEM STATUS Demonstration
     */
    private void demonstrateSystemStatus() {
        System.out.println("\n📊 SYSTEM STATUS Demonstration");
        System.out.println("-".repeat(40));
        
        // Show available spots
        var availableSpots = parkingLotService.getAvailableSpotCount();
        System.out.println("Available spots by type:");
        availableSpots.forEach((type, count) -> 
            System.out.printf("   %s: %d spots%n", type.getDisplayName(), count));
        
        // Show occupancy rate
        double occupancyRate = parkingLotService.getOccupancyRate();
        System.out.printf("\nOccupancy Rate: %.1f%%%n", occupancyRate);
        
        // Show active tickets
        int activeTickets = parkingLotService.getActiveTickets().size();
        System.out.printf("Active Tickets: %d%n", activeTickets);
        
        System.out.println("\n🎉 Demo completed successfully!");
        System.out.println("✅ All design patterns demonstrated:");
        System.out.println("   • Factory Pattern ✅");
        System.out.println("   • Strategy Pattern ✅");
        System.out.println("   • Observer Pattern ✅");
        System.out.println("   • Singleton Pattern ✅ (Spring Services)");
        System.out.println("   • Facade Pattern ✅ (REST Controller)");
        System.out.println("\n✅ SOLID Principles applied:");
        System.out.println("   • Single Responsibility ✅");
        System.out.println("   • Open/Closed ✅");
        System.out.println("   • Liskov Substitution ✅");
        System.out.println("   • Interface Segregation ✅");
        System.out.println("   • Dependency Inversion ✅");
    }
}
