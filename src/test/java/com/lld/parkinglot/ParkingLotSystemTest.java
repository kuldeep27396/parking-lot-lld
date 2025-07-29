package com.lld.parkinglot;

import com.lld.parkinglot.factory.VehicleFactory;
import com.lld.parkinglot.models.*;
import com.lld.parkinglot.enums.*;
import com.lld.parkinglot.strategy.payment.CashPaymentStrategy;
import com.lld.parkinglot.strategy.pricing.TimeBasedPricingStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UNIT TESTS - Demonstrates testing of design patterns
 * 
 * These tests show how well-designed code with proper separation
 * of concerns is easy to test.
 */
@SpringBootTest
class ParkingLotSystemTest {
    
    private VehicleFactory vehicleFactory;
    private TimeBasedPricingStrategy pricingStrategy;
    private CashPaymentStrategy paymentStrategy;
    
    @BeforeEach
    void setUp() {
        vehicleFactory = new VehicleFactory();
        pricingStrategy = new TimeBasedPricingStrategy();
        paymentStrategy = new CashPaymentStrategy();
    }
    
    /**
     * Test FACTORY PATTERN
     */
    @Test
    void testVehicleFactory() {
        // Test creating different vehicle types
        Vehicle car = vehicleFactory.createVehicle(VehicleType.CAR, "ABC123", "Red", "Honda");
        Vehicle motorcycle = vehicleFactory.createVehicle(VehicleType.MOTORCYCLE, "BIKE456", "Blue", "Yamaha");
        Vehicle truck = vehicleFactory.createVehicle(VehicleType.TRUCK, "TRUCK789", "White", "Ford");
        
        // Assertions
        assertNotNull(car);
        assertNotNull(motorcycle);
        assertNotNull(truck);
        
        assertEquals(VehicleType.CAR, car.getVehicleType());
        assertEquals(VehicleType.MOTORCYCLE, motorcycle.getVehicleType());
        assertEquals(VehicleType.TRUCK, truck.getVehicleType());
        
        assertEquals("ABC123", car.getLicenseNumber());
        assertEquals("BIKE456", motorcycle.getLicenseNumber());
        assertEquals("TRUCK789", truck.getLicenseNumber());
    }
    
    /**
     * Test INHERITANCE and POLYMORPHISM
     */
    @Test
    void testVehiclePolymorphism() {
        Vehicle car = vehicleFactory.createCar("CAR123", "Blue", "Toyota");
        Vehicle motorcycle = vehicleFactory.createMotorcycle("BIKE123", "Red", "Honda");
        Vehicle truck = vehicleFactory.createTruck("TRUCK123", "Green", "Volvo");
        
        // Test polymorphic behavior - same method, different implementations
        assertTrue(car.getParkingSpaceRequired() >= 1);
        assertTrue(motorcycle.getParkingSpaceRequired() >= 1);
        assertTrue(truck.getParkingSpaceRequired() >= 2);
        
        // Test that each vehicle has its own display format
        assertNotEquals(car.getDisplayInfo(), motorcycle.getDisplayInfo());
        assertTrue(car.getDisplayInfo().contains("🚗"));
        assertTrue(motorcycle.getDisplayInfo().contains("🏍️"));
        assertTrue(truck.getDisplayInfo().contains("🚛"));
    }
    
    /**
     * Test ENCAPSULATION
     */
    @Test
    void testParkingSpotEncapsulation() {
        ParkingSpot spot = new ParkingSpot("A-1-001", ParkingSpotType.CAR_SPOT, 1);
        Vehicle car = vehicleFactory.createCar("TEST123", "White", "Test Car");
        
        // Test initial state
        assertTrue(spot.isAvailable());
        assertFalse(spot.isReserved());
        assertNull(spot.getCurrentVehicle());
        
        // Test controlled state change through public methods
        assertTrue(spot.canAccommodate(car));
        assertTrue(spot.parkVehicle(car));
        
        // Test state after parking
        assertFalse(spot.isAvailable());
        assertNotNull(spot.getCurrentVehicle());
        assertEquals(car, spot.getCurrentVehicle());
        
        // Test unparking
        assertTrue(spot.unparkVehicle());
        assertTrue(spot.isAvailable());
        assertNull(spot.getCurrentVehicle());
    }
    
    /**
     * Test STRATEGY PATTERN - Pricing
     */
    @Test
    void testPricingStrategy() {
        // Create test vehicles
        Vehicle car = vehicleFactory.createCar("CAR123", "Blue", "Honda");
        Vehicle motorcycle = vehicleFactory.createMotorcycle("BIKE123", "Red", "Yamaha");
        Vehicle truck = vehicleFactory.createTruck("TRUCK123", "White", "Ford");
        
        // Create parking spots
        ParkingSpot carSpot = new ParkingSpot("C-1-001", ParkingSpotType.CAR_SPOT, 1);
        ParkingSpot bikeSpot = new ParkingSpot("M-1-001", ParkingSpotType.MOTORCYCLE_SPOT, 1);
        ParkingSpot truckSpot = new ParkingSpot("T-1-001", ParkingSpotType.TRUCK_SPOT, 1);
        
        // Create tickets
        ParkingTicket carTicket = new ParkingTicket("TKT001", car, carSpot, "1234567890");
        ParkingTicket bikeTicket = new ParkingTicket("TKT002", motorcycle, bikeSpot, "1234567890");
        ParkingTicket truckTicket = new ParkingTicket("TKT003", truck, truckSpot, "1234567890");
        
        // Test pricing strategy
        double carRate = pricingStrategy.getHourlyRate(carTicket);
        double bikeRate = pricingStrategy.getHourlyRate(bikeTicket);
        double truckRate = pricingStrategy.getHourlyRate(truckTicket);
        
        // Assertions - trucks should cost more than cars, cars more than motorcycles
        assertTrue(truckRate > carRate);
        assertTrue(carRate > bikeRate);
        assertTrue(bikeRate > 0);
    }
    
    /**
     * Test STRATEGY PATTERN - Payment
     */
    @Test
    void testPaymentStrategy() {
        Vehicle car = vehicleFactory.createCar("PAY123", "Silver", "Test Car");
        ParkingSpot spot = new ParkingSpot("P-1-001", ParkingSpotType.CAR_SPOT, 1);
        ParkingTicket ticket = new ParkingTicket("PAY001", car, spot, "1234567890");
        
        double amount = 10.0;
        
        // Test cash payment strategy
        assertTrue(paymentStrategy.canProcessPayment(amount));
        assertEquals(PaymentMethod.CASH, paymentStrategy.getPaymentMethod());
        assertEquals(0.0, paymentStrategy.calculateAdditionalFees(amount)); // Cash has no fees
        
        Payment payment = paymentStrategy.processPayment(ticket, amount);
        
        assertNotNull(payment);
        assertEquals(amount, payment.getAmount());
        assertEquals(PaymentMethod.CASH, payment.getPaymentMethod());
        assertTrue(payment.isSuccessful());
    }
    
    /**
     * Test BUSINESS LOGIC and VALIDATION
     */
    @Test
    void testBusinessRules() {
        // Test vehicle validation
        assertTrue(vehicleFactory.isValidVehicleData(VehicleType.CAR, "ABC123", "Red", "Honda"));
        assertFalse(vehicleFactory.isValidVehicleData(VehicleType.CAR, "", "Red", "Honda")); // Empty license
        assertFalse(vehicleFactory.isValidVehicleData(null, "ABC123", "Red", "Honda")); // Null type
        
        // Test parking spot accommodation rules
        ParkingSpot carSpot = new ParkingSpot("C-1-001", ParkingSpotType.CAR_SPOT, 1);
        ParkingSpot motorSpot = new ParkingSpot("M-1-001", ParkingSpotType.MOTORCYCLE_SPOT, 1);
        ParkingSpot truckSpot = new ParkingSpot("T-1-001", ParkingSpotType.TRUCK_SPOT, 1);
        
        Vehicle car = vehicleFactory.createCar("CAR123", "Blue", "Honda");
        Vehicle motorcycle = vehicleFactory.createMotorcycle("BIKE123", "Red", "Yamaha");
        Vehicle truck = vehicleFactory.createTruck("TRUCK123", "White", "Ford");
        
        // Car spots can accommodate cars and motorcycles, but not trucks
        assertTrue(carSpot.canAccommodate(car));
        assertTrue(carSpot.canAccommodate(motorcycle));
        assertFalse(carSpot.canAccommodate(truck));
        
        // Motorcycle spots can only accommodate motorcycles
        assertFalse(motorSpot.canAccommodate(car));
        assertTrue(motorSpot.canAccommodate(motorcycle));
        assertFalse(motorSpot.canAccommodate(truck));
        
        // Truck spots can accommodate all vehicle types
        assertTrue(truckSpot.canAccommodate(car));
        assertTrue(truckSpot.canAccommodate(motorcycle));
        assertTrue(truckSpot.canAccommodate(truck));
    }
    
    /**
     * Test ERROR HANDLING
     */
    @Test
    void testErrorHandling() {
        // Test invalid vehicle creation
        assertThrows(IllegalArgumentException.class, () -> {
            vehicleFactory.createVehicle(VehicleType.CAR, null, "Red", "Honda");
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            vehicleFactory.createVehicle(VehicleType.CAR, "", "Red", "Honda");
        });
        
        // Test payment validation
        assertFalse(paymentStrategy.canProcessPayment(-1.0)); // Negative amount
        assertFalse(paymentStrategy.canProcessPayment(0.0));  // Zero amount
    }
}
