# 🚗 Parking Lot Management System - Complete LLD & OOP Guide

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![H2 Database](https://img.shields.io/badge/Database-H2-blue.svg)](https://www.h2database.com/)
[![Maven](https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)

> **A comprehensive Spring Boot application demonstrating all major Low-Level Design (LLD) principles and Object-Oriented Programming (OOP) concepts through a real-world parking lot management system.**

---

## 📋 Table of Contents

1. [🎯 Project Overview](#-project-overview)
2. [🏗️ Project Structure & Design Rationale](#️-project-structure--design-rationale)
3. [🧱 Design Patterns Implemented](#-design-patterns-implemented)
4. [🎭 SOLID Principles](#-solid-principles)
5. [🔄 OOP Concepts](#-oop-concepts)
6. [🗄️ Database Design](#️-database-design)
7. [🚀 Quick Start Guide](#-quick-start-guide)
8. [🔧 API Documentation](#-api-documentation)
9. [📊 System Features](#-system-features)
10. [🧪 Testing](#-testing)
11. [📚 Learning Resources](#-learning-resources)
12. [🔮 Extension Ideas](#-extension-ideas)

---

## 🎯 Project Overview

This project is a **production-ready parking lot management system** built with Spring Boot that serves as a comprehensive learning resource for:

- **Low-Level Design (LLD)** principles
- **Object-Oriented Programming (OOP)** concepts
- **Design Patterns** implementation
- **SOLID Principles** application
- **Spring Boot** enterprise development
- **RESTful API** design
- **Database modeling** and JPA relationships

### Why This Project?

Most LLD tutorials show isolated examples. This project demonstrates how **all concepts work together** in a real-world application that you can actually run, test, and extend.

---

## 🏗️ Project Structure & Design Rationale

### Current Structure

```
src/
├── main/
│   ├── java/com/lld/parkinglot/
│   │   ├── ParkingLotApplication.java       # Spring Boot Entry Point
│   │   ├── ParkingLotDemo.java             # Console Demo Runner
│   │   ├── controller/                      # REST API Layer (Facade Pattern)
│   │   │   └── ParkingLotController.java
│   │   ├── service/                        # Business Logic Layer
│   │   │   └── ParkingLotService.java
│   │   ├── models/                         # Domain Entities (OOP Showcase)
│   │   │   ├── Vehicle.java                # Abstract Base Class
│   │   │   ├── Car.java                    # Concrete Implementation
│   │   │   ├── Motorcycle.java             # Concrete Implementation  
│   │   │   ├── Truck.java                  # Concrete Implementation
│   │   │   ├── ParkingSpot.java            # Entity with Relationships
│   │   │   ├── ParkingTicket.java          # Business Entity
│   │   │   ├── Payment.java                # Financial Entity
│   │   │   └── NotificationEvent.java      # Event Entity
│   │   ├── enums/                          # Type Safety & Constants
│   │   │   ├── VehicleType.java
│   │   │   ├── ParkingSpotType.java
│   │   │   ├── ParkingSpotStatus.java
│   │   │   └── PaymentMethod.java
│   │   ├── factory/                        # Creational Patterns
│   │   │   └── VehicleFactory.java
│   │   ├── strategy/                       # Behavioral Patterns
│   │   │   ├── TimeBasedPricingStrategy.java
│   │   │   └── CashPaymentStrategy.java
│   │   ├── observer/                       # Behavioral Patterns
│   │   │   └── EmailNotificationObserver.java
│   │   └── interfaces/                     # Contracts & Abstractions
│   │       ├── PricingStrategy.java
│   │       ├── PaymentStrategy.java
│   │       ├── NotificationObserver.java
│   │       └── Parkable.java
│   └── resources/
│       └── application.properties          # Configuration
└── test/                                   # Unit Tests
    └── java/com/lld/parkinglot/
        └── ParkingLotSystemTest.java
```

### 🤔 Why This Structure?

#### **1. Layered Architecture**
- **Controller Layer**: Handles HTTP requests (Presentation)
- **Service Layer**: Contains business logic (Business)  
- **Model Layer**: Domain entities and data (Data)

**Benefits**: Separation of concerns, easier testing, maintainable code

#### **2. Package by Feature vs Package by Layer**
We chose **package by layer** because:
- ✅ Easier for beginners to understand
- ✅ Clear separation of responsibilities
- ✅ Standard Spring Boot convention

**Alternative**: Package by feature would be:
```
parking/
payment/
notification/
vehicle/
```

#### **3. Why Separate Interfaces Package?**
- **Dependency Inversion**: Depend on abstractions, not concretions
- **Testability**: Easy to mock interfaces
- **Flexibility**: Swap implementations without changing client code

---

## 🧱 Design Patterns Implemented

### 🏭 Creational Patterns

#### 1. **Factory Pattern** 
📂 [`VehicleFactory.java`](src/main/java/com/lld/parkinglot/factory/VehicleFactory.java)

```java
// Why Factory? - Encapsulates object creation logic
Vehicle vehicle = vehicleFactory.createVehicle(
    VehicleType.CAR, "ABC123", "Red", "Honda Civic", 
    4, true  // doors, electric
);
```

**Problem Solved**: Creating different vehicle types without exposing creation logic
**Alternative Patterns**: Abstract Factory, Builder Pattern

#### 2. **Singleton Pattern (Implicit)**
📂 [`ParkingLotService.java`](src/main/java/com/lld/parkinglot/service/ParkingLotService.java)

Spring's `@Service` annotation creates singleton beans automatically.

**Why**: Ensure single instance of parking lot service across application

---

### 🔧 Structural Patterns

#### 1. **Facade Pattern**
📂 [`ParkingLotController.java`](src/main/java/com/lld/parkinglot/controller/ParkingLotController.java)

```java
@RestController
public class ParkingLotController {
    // Simple API hiding complex subsystem
    @PostMapping("/enter")
    public ResponseEntity<?> parkVehicle(@RequestBody ParkVehicleRequest request)
}
```

**Problem Solved**: Provides simple interface to complex parking lot subsystem
**Benefits**: Decoupling, easier to use, hides complexity

---

### ⚡ Behavioral Patterns

#### 1. **Strategy Pattern**
📂 [`PricingStrategy.java`](src/main/java/com/lld/parkinglot/interfaces/PricingStrategy.java) | [`TimeBasedPricingStrategy.java`](src/main/java/com/lld/parkinglot/strategy/TimeBasedPricingStrategy.java)

```java
public interface PricingStrategy {
    double calculatePrice(long durationMinutes, VehicleType vehicleType);
}

// Different pricing algorithms can be plugged in
@Component
public class TimeBasedPricingStrategy implements PricingStrategy {
    // Time-based pricing logic
}
```

**Problem Solved**: Different pricing algorithms without changing client code
**Extensions**: WeekendPricingStrategy, HolidayPricingStrategy

#### 2. **Observer Pattern**  
📂 [`NotificationObserver.java`](src/main/java/com/lld/parkinglot/interfaces/NotificationObserver.java) | [`EmailNotificationObserver.java`](src/main/java/com/lld/parkinglot/observer/EmailNotificationObserver.java)

```java
public interface NotificationObserver {
    void update(NotificationEvent event);
}

// Observers get notified of parking events
@Component  
public class EmailNotificationObserver implements NotificationObserver {
    public void update(NotificationEvent event) {
        // Send email notification
    }
}
```

**Problem Solved**: Loose coupling between event source and handlers
**Extensions**: SMSObserver, PushNotificationObserver

#### 3. **Template Method Pattern**
📂 [`Vehicle.java`](src/main/java/com/lld/parkinglot/models/Vehicle.java)

```java
public abstract class Vehicle {
    // Template method - common structure
    public boolean isValidForParking() {
        return isValidLicenseNumber() && hasValidDocuments();
    }
    
    // Abstract methods - subclass specific
    public abstract int getParkingSpaceRequired();
    public abstract String getDisplayInfo();
}
```

**Problem Solved**: Define skeleton of algorithm, let subclasses override specific steps

---

## 🎭 SOLID Principles

### **S - Single Responsibility Principle**

Each class has **one reason to change**:

- 📂 [`Vehicle.java`](src/main/java/com/lld/parkinglot/models/Vehicle.java): Only handles vehicle data and behavior
- 📂 [`ParkingLotService.java`](src/main/java/com/lld/parkinglot/service/ParkingLotService.java): Only handles parking business logic  
- 📂 [`ParkingLotController.java`](src/main/java/com/lld/parkinglot/controller/ParkingLotController.java): Only handles HTTP requests/responses

### **O - Open/Closed Principle**

**Open for extension, closed for modification**:

```java
// Adding new vehicle type doesn't require changing existing code
public class ElectricCar extends Vehicle {
    @Override
    public int getParkingSpaceRequired() {
        return 2; // Needs charging station space
    }
}
```

### **L - Liskov Substitution Principle**

**Subtypes must be substitutable for their base types**:

```java
// Any Vehicle subtype can be used wherever Vehicle is expected
Vehicle vehicle = new Car(...);  // ✅ Works
vehicle = new Motorcycle(...);   // ✅ Works  
vehicle = new Truck(...);        // ✅ Works
```

### **I - Interface Segregation Principle**

**Clients shouldn't depend on interfaces they don't use**:

- 📂 [`Parkable.java`](src/main/java/com/lld/parkinglot/interfaces/Parkable.java): Only parking-related methods
- 📂 [`PricingStrategy.java`](src/main/java/com/lld/parkinglot/interfaces/PricingStrategy.java): Only pricing-related methods
- 📂 [`PaymentStrategy.java`](src/main/java/com/lld/parkinglot/interfaces/PaymentStrategy.java): Only payment-related methods

### **D - Dependency Inversion Principle**

**Depend on abstractions, not concretions**:

```java
@Service
public class ParkingLotService {
    // Depends on interface, not concrete implementation
    @Autowired
    private PricingStrategy pricingStrategy;  // ✅ Good
    
    // NOT: private TimeBasedPricingStrategy pricing; // ❌ Bad
}
```

---

## 🔄 OOP Concepts

### **1. Encapsulation**
📂 [`Vehicle.java`](src/main/java/com/lld/parkinglot/models/Vehicle.java)

```java
public abstract class Vehicle {
    private String licenseNumber;  // Private fields
    private VehicleType vehicleType;
    
    // Controlled access through getters
    public String getLicenseNumber() { return licenseNumber; }
    
    // Business logic encapsulated
    public boolean isValidLicenseNumber() {
        return licenseNumber != null && 
               licenseNumber.matches("^[A-Z0-9]{6,10}$");
    }
}
```

### **2. Inheritance**
📂 Vehicle Hierarchy: [`Vehicle.java`](src/main/java/com/lld/parkinglot/models/Vehicle.java) → [`Car.java`](src/main/java/com/lld/parkinglot/models/Car.java), [`Motorcycle.java`](src/main/java/com/lld/parkinglot/models/Motorcycle.java), [`Truck.java`](src/main/java/com/lld/parkinglot/models/Truck.java)

```java
// Base class defines common structure
public abstract class Vehicle { /*...*/ }

// Specialized implementations
public class Car extends Vehicle {
    @Override
    public int getParkingSpaceRequired() {
        return isElectric ? 2 : 1;  // Electric cars need charging space
    }
}

public class Truck extends Vehicle {
    @Override  
    public int getParkingSpaceRequired() {
        return cargoCapacity > 15 ? 3 : 2;  // Large trucks need more space
    }
}
```

### **3. Polymorphism**

**Runtime polymorphism** through method overriding:

```java
List<Vehicle> vehicles = Arrays.asList(
    new Car("ABC123", "Red", "Honda", 4, false),
    new Motorcycle("XYZ789", "Black", "Yamaha", false),
    new Truck("TRK456", "White", "Ford", 12.5, 4)
);

// Each vehicle behaves differently at runtime
for (Vehicle vehicle : vehicles) {
    System.out.println(vehicle.getDisplayInfo());  // Polymorphic call
    int spacesNeeded = vehicle.getParkingSpaceRequired();  // Different for each type
}
```

### **4. Abstraction**

**Interface-based abstraction**:
📂 [`PricingStrategy.java`](src/main/java/com/lld/parkinglot/interfaces/PricingStrategy.java)

```java
// Abstract interface - clients don't know implementation details
public interface PricingStrategy {
    double calculatePrice(long durationMinutes, VehicleType vehicleType);
}

// Implementation hidden from clients
@Component
public class TimeBasedPricingStrategy implements PricingStrategy {
    // Complex pricing logic hidden here
}
```

---

## 🗄️ Database Design

### **Entity Relationship Diagram**

```
┌─────────────┐     ┌──────────────┐     ┌─────────────┐     ┌─────────────┐
│   Vehicle   │────▶│ ParkingSpot  │────▶│ParkingTicket│────▶│   Payment   │
│             │ 1:1 │              │ 1:1 │             │ 1:1 │             │
│license_num  │     │spot_number   │     │ticket_num   │     │payment_id   │
│vehicle_type │     │spot_type     │     │entry_time   │     │amount       │
│color        │     │floor_level   │     │exit_time    │     │method       │
│model        │     │is_available  │     │is_active    │     │status       │
│...          │     │...           │     │...          │     │...          │
└─────────────┘     └──────────────┘     └─────────────┘     └─────────────┘
```

### **System Architecture Diagram**

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           🌐 REST API Layer                            │
│  ┌─────────────────────────────────────────────────────────────────────┐ │
│  │                    ParkingLotController                             │ │
│  │  GET /status  │  POST /enter  │  POST /exit  │  POST /reserve     │ │
│  └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         🏢 Business Logic Layer                        │
│  ┌─────────────────────────────────────────────────────────────────────┐ │
│  │                      ParkingLotService                              │ │
│  │  • Vehicle Management    • Spot Assignment    • Pricing Logic      │ │
│  │  • Notification Events   • Payment Processing • Validation Rules   │ │
│  └─────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────┘
         │                    │                    │                    │
         ▼                    ▼                    ▼                    ▼
┌──────────────┐    ┌──────────────┐    ┌──────────────┐    ┌──────────────┐
│   🏭 Factory  │    │  ⚡ Strategy  │    │  👁️ Observer  │    │  🗄️ Repository│
│   Patterns    │    │   Patterns    │    │   Patterns    │    │    Layer     │
│              │    │              │    │              │    │              │
│ VehicleFactory│    │PricingStrategy│    │Notification  │    │  JPA Entities│
│              │    │PaymentStrategy│    │ Observer     │    │  H2 Database │
└──────────────┘    └──────────────┘    └──────────────┘    └──────────────┘
```

### **Class Hierarchy Diagram**

```
                              🚗 Vehicle (Abstract)
                                     │
                    ┌────────────────┼────────────────┐
                    │                │                │
              🚗 Car (Concrete) 🏍️ Motorcycle    🚛 Truck (Concrete)
              │                   (Concrete)           │
              │                      │                │
        ┌─────────────┐         ┌─────────┐     ┌─────────────┐
        │- doors: int │         │- sidecar│     │- capacity   │
        │- isElectric │         │ : boolean│     │ : double    │
        │             │         │         │     │- axles: int │
        └─────────────┘         └─────────┘     └─────────────┘

                         🔗 Common Interface Implementations:
                              ┌─ Parkable
                              ├─ Displayable  
                              └─ Validatable
```

### **Design Pattern Flow Diagram**

```
   🎯 Client Request
         │
         ▼
   ┌─────────────┐     📋 Facade Pattern
   │ Controller  │ ◄── Simplifies Complex Subsystem
   └─────────────┘
         │
         ▼
   ┌─────────────┐     🏭 Factory Pattern
   │  Service    │ ──► Creates Vehicle Objects
   └─────────────┘
         │
         ▼
   ┌─────────────┐     ⚡ Strategy Pattern
   │ Pricing/    │ ◄── Pluggable Algorithms
   │ Payment     │
   └─────────────┘
         │
         ▼
   ┌─────────────┐     👁️ Observer Pattern
   │Notification │ ◄── Event-Driven Updates
   │ System      │
   └─────────────┘
         │
         ▼
   🎉 Response/Notification
```

### **JPA Entity Mappings**

#### **Inheritance Strategy**: Single Table
📂 [`Vehicle.java`](src/main/java/com/lld/parkinglot/models/Vehicle.java)

```java
@Entity
@Table(name = "vehicles")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vehicle_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Vehicle {
    @Id
    private String licenseNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", insertable = false, updatable = false)
    private VehicleType vehicleType;
}

@Entity
@DiscriminatorValue("CAR")
public class Car extends Vehicle { /*...*/ }
```

**Why Single Table?**
- ✅ Better performance (no joins)
- ✅ Simple queries
- ❌ Sparse table (unused columns)

**Alternatives**:
- **Table Per Class**: Separate table for each entity
- **Joined**: Normalized approach with joins

#### **Foreign Key Relationships**

```sql
-- Auto-generated by Hibernate
ALTER TABLE parking_spots 
    ADD CONSTRAINT FK_current_vehicle 
    FOREIGN KEY (current_vehicle_id) 
    REFERENCES vehicles(license_number);

ALTER TABLE parking_tickets 
    ADD CONSTRAINT FK_parking_spot 
    FOREIGN KEY (parking_spot_id) 
    REFERENCES parking_spots(id);
```

---

## 🚀 Quick Start Guide

### **Prerequisites**

- ☕ **Java 21+** ([Download](https://adoptium.net/))
- 🔧 **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- 💻 **IDE**: IntelliJ IDEA, Eclipse, or VS Code

### **1. Clone & Run**

```bash
# Clone the repository
git clone https://github.com/kuldeep27396/parking-lot-lld.git
cd parking-lot-lld

# Run the application
mvn spring-boot:run
```

### **2. Access Points**

- 🌐 **Main Application**: http://localhost:8080/parking-lot
- 🗃️ **H2 Database Console**: http://localhost:8080/parking-lot/h2-console
  - **JDBC URL**: `jdbc:h2:mem:parking_lot_db`
  - **Username**: `sa`
  - **Password**: `password`
- 📊 **API Status**: http://localhost:8080/parking-lot/api/parking/status

### **3. Database Console Access**

1. Go to H2 Console URL
2. Use connection details from above
3. Explore tables: `VEHICLES`, `PARKING_SPOTS`, `PARKING_TICKETS`, `PAYMENTS`

---

## 🔧 API Documentation

### **Base URL**: `http://localhost:8080/parking-lot/api/parking`

### **1. Get Parking Status**
```http
GET /status
```

**Response**:
```json
{
  "availableSpots": {
    "MOTORCYCLE_SPOT": 250,
    "CAR_SPOT": 500,
    "TRUCK_SPOT": 100,
    "HANDICAPPED_SPOT": 50
  },
  "occupancyRate": 0.0,
  "activeTickets": 0
}
```

### **2. Park Vehicle**
```http
POST /enter
Content-Type: application/json

{
  "licenseNumber": "ABC123",
  "vehicleType": "CAR",
  "color": "Red",
  "model": "Honda Civic"
}
```

**Response**:
```json
{
  "ticketNumber": "TKT-1234567890",
  "spotNumber": "C-001-015",
  "entryTime": "2025-07-28T10:30:00",
  "vehicleInfo": "🚗 Car: ABC123 [Red] Honda Civic (4 doors) ✅"
}
```

### **3. Exit Vehicle**
```http
POST /exit
Content-Type: application/json

{
  "licenseNumber": "ABC123",
  "paymentMethod": "CREDIT_CARD"
}
```

### **4. Reserve Spot**
```http
POST /reserve
Content-Type: application/json

{
  "vehicleType": "CAR",
  "durationMinutes": 120
}
```

---

## 📊 System Features

### **🚗 Vehicle Management**
- **Multi-type Support**: Cars, Motorcycles, Trucks
- **Vehicle-specific Logic**: Different space requirements, pricing
- **Validation**: License number format, vehicle constraints

### **🅿️ Parking Operations**
- **900 Total Spots**: Across 5 floors
- **Spot Types**: Regular, Handicapped, Motorcycle, Truck
- **Real-time Availability**: Live spot tracking
- **Automatic Assignment**: Finds best available spot

### **💰 Pricing & Payments**
- **Time-based Pricing**: Different rates by vehicle type
- **Multiple Payment Methods**: Cash, Credit Card, Digital Wallet
- **Transaction Tracking**: Complete payment history

### **🔔 Notifications**
- **Event-driven**: Observer pattern implementation
- **Multiple Channels**: Email, SMS (extensible)
- **Real-time Updates**: Parking events trigger notifications

### **📈 Analytics & Reporting**
- **Occupancy Rates**: Track utilization
- **Revenue Tracking**: Financial reporting
- **Usage Patterns**: Peak hours, popular spots

---

## 🧪 Testing

### **Run Tests**
```bash
# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### **Test Structure**
📂 [`ParkingLotSystemTest.java`](src/test/java/com/lld/parkinglot/ParkingLotSystemTest.java)

```java
@SpringBootTest
class ParkingLotSystemTest {
    
    @Test
    void testVehicleFactoryCreation() {
        // Test Factory Pattern
    }
    
    @Test  
    void testParkingOperations() {
        // Test core parking logic
    }
    
    @Test
    void testPricingStrategy() {
        // Test Strategy Pattern
    }
}
```

### **API Testing with cURL**

Use the provided test script:
```bash
chmod +x test_apis.sh
./test_apis.sh
```

---

## 📚 Learning Resources

### **Design Patterns Deep Dive**

#### **When to Use Each Pattern**

| Pattern | Use When | Examples in Code |
|---------|----------|------------------|
| **Factory** | Creating related objects | [`VehicleFactory.java`](src/main/java/com/lld/parkinglot/factory/VehicleFactory.java) |
| **Strategy** | Swappable algorithms | [`PricingStrategy.java`](src/main/java/com/lld/parkinglot/interfaces/PricingStrategy.java) |
| **Observer** | Event notifications | [`NotificationObserver.java`](src/main/java/com/lld/parkinglot/interfaces/NotificationObserver.java) |
| **Facade** | Simplifying complex APIs | [`ParkingLotController.java`](src/main/java/com/lld/parkinglot/controller/ParkingLotController.java) |

#### **Study Path**

1. **Start with**: OOP concepts in vehicle hierarchy
2. **Then explore**: Factory pattern for object creation
3. **Understand**: Strategy pattern for pricing
4. **Observe**: Observer pattern for notifications
5. **See big picture**: How Facade pattern ties it all together

### **Real-world Applications**

- **E-commerce**: Product factories, pricing strategies, order observers
- **Banking**: Account hierarchies, transaction strategies, notification systems
- **Gaming**: Character factories, AI strategies, event systems

### **🎨 Design Pattern Interaction Diagram**

```
    Factory Pattern          Strategy Pattern         Observer Pattern
         │                        │                       │
         ▼                        ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│ VehicleFactory  │    │ PricingStrategy │    │NotificationObsvr│
│                 │    │                 │    │                 │
│ createVehicle() │───▶│ calculatePrice()│───▶│ update(event)   │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                        │                       │
         ▼                        ▼                       ▼
    Creates Objects         Calculates Fees        Sends Notifications
         │                        │                       │
         └─────────────┬──────────┴───────────────────────┘
                       ▼
              ┌─────────────────┐
              │ ParkingLotService│ ◄── Facade Pattern
              │                 │     (Coordinates All)
              │ parkVehicle()   │
              │ exitVehicle()   │
              └─────────────────┘
```

### **🤔 Design Questions & Answers**

#### **Q1: Why use Factory Pattern instead of direct object creation?**

**❌ Without Factory:**
```java
// Tight coupling, violates Open/Closed Principle
if (type == VehicleType.CAR) {
    return new Car(license, color, model, doors, isElectric);
} else if (type == VehicleType.MOTORCYCLE) {
    return new Motorcycle(license, color, model, hasSidecar);
} else if (type == VehicleType.TRUCK) {
    return new Truck(license, color, model, capacity, axles);
}
```

**✅ With Factory:**
```java
// Loose coupling, easy to extend
Vehicle vehicle = vehicleFactory.createVehicle(type, params...);
```

**Benefits:**
- **Encapsulation**: Creation logic in one place
- **Extensibility**: Add new vehicle types without changing client code
- **Testability**: Easy to mock factory

#### **Q2: Why Strategy Pattern over if-else for pricing?**

**❌ Without Strategy:**
```java
public double calculatePrice(VehicleType type, long duration) {
    if (type == VehicleType.CAR) {
        return duration * 2.0; // Hard-coded logic
    } else if (type == VehicleType.TRUCK) {
        return duration * 5.0; // Violates Open/Closed
    }
    // More if-else statements...
}
```

**✅ With Strategy:**
```java
@Autowired
private PricingStrategy pricingStrategy; // Pluggable algorithms

public double calculatePrice(VehicleType type, long duration) {
    return pricingStrategy.calculatePrice(duration, type);
}
```

**Benefits:**
- **Runtime flexibility**: Change pricing algorithms
- **Single Responsibility**: Each strategy has one pricing logic
- **Testing**: Test each strategy independently

#### **Q3: Why Observer Pattern for notifications?**

**❌ Without Observer:**
```java
public ParkingTicket parkVehicle(Vehicle vehicle) {
    // Business logic
    ParkingTicket ticket = createTicket(vehicle);
    
    // Tightly coupled notification code
    emailService.sendEmail(vehicle.getOwner(), "Parked successfully");
    smsService.sendSMS(vehicle.getOwner(), "Parked successfully");
    pushService.sendPush(vehicle.getOwner(), "Parked successfully");
    
    return ticket;
}
```

**✅ With Observer:**
```java
public ParkingTicket parkVehicle(Vehicle vehicle) {
    // Business logic
    ParkingTicket ticket = createTicket(vehicle);
    
    // Loose coupling via events
    notifyObservers(new NotificationEvent(VEHICLE_PARKED, vehicle));
    
    return ticket;
}
```

**Benefits:**
- **Decoupling**: Business logic separated from notifications
- **Extensibility**: Add new notification channels easily
- **Event-driven**: Natural fit for reactive systems

#### **Q4: How does this design handle the Single Responsibility Principle?**

**Each class has ONE reason to change:**

| Class | Responsibility | Changes When |
|-------|----------------|--------------|
| `Vehicle` | Vehicle data/behavior | Vehicle attributes change |
| `ParkingSpot` | Spot management | Spot properties change |
| `PricingStrategy` | Price calculation | Pricing rules change |
| `NotificationObserver` | Send notifications | Notification logic changes |
| `ParkingLotController` | HTTP handling | API contract changes |
| `ParkingLotService` | Business orchestration | Business rules change |

#### **Q5: How does the design support the Open/Closed Principle?**

**Open for Extension, Closed for Modification:**

```java
// Adding new vehicle type - NO existing code changes needed
public class ElectricScooter extends Vehicle {
    @Override
    public int getParkingSpaceRequired() {
        return 1; // Scooter-specific logic
    }
    
    @Override
    public String getDisplayInfo() {
        return "🛴 Electric Scooter: " + getLicenseNumber();
    }
}

// Adding new pricing strategy - NO existing code changes needed
@Component
public class HolidayPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(long duration, VehicleType type) {
        // Holiday-specific pricing with 50% surcharge
        return new TimeBasedPricingStrategy().calculatePrice(duration, type) * 1.5;
    }
}

// Adding new notification channel - NO existing code changes needed
@Component
public class SlackNotificationObserver implements NotificationObserver {
    @Override
    public void update(NotificationEvent event) {
        // Send Slack notification
    }
}
```

#### **Q6: Why use Abstract classes vs Interfaces?**

**When we chose Abstract Class (`Vehicle`):**
- **Common implementation**: Shared fields and methods
- **Template methods**: Common algorithm with customizable steps
- **State sharing**: All vehicles have license, color, model

**When we chose Interface (`PricingStrategy`):**
- **Pure contracts**: No shared implementation needed
- **Multiple inheritance**: Classes can implement multiple interfaces
- **Plugin architecture**: Swap implementations at runtime

#### **Q7: How does this design demonstrate Polymorphism?**

**Runtime Polymorphism in Action:**

```java
// Same interface, different behaviors at runtime
List<Vehicle> vehicles = Arrays.asList(
    new Car("ABC123", "Red", "Honda", 4, false),
    new Motorcycle("XYZ789", "Black", "Yamaha", false),
    new Truck("TRK456", "White", "Ford", 12.5, 4)
);

for (Vehicle vehicle : vehicles) {
    // Method resolved at runtime based on actual object type
    System.out.println(vehicle.getDisplayInfo());
    /*
     * Output:
     * 🚗 Car: ABC123 [Red] Honda (4 doors) ❌
     * 🏍️ Motorcycle: XYZ789 [Black] Yamaha ❌
     * 🚛 Truck: TRK456 [White] Ford (12.5 tons, 4 axles)
     */
    
    int spaces = vehicle.getParkingSpaceRequired();
    // Car: 1, Motorcycle: 1, Truck: 2-3 (based on capacity)
}
```

#### **Q8: What alternative architectural patterns could be used?**

| Pattern | Use Case | Trade-offs |
|---------|----------|------------|
| **Hexagonal Architecture** | Clean separation of business logic | More complex, better testability |
| **Event Sourcing** | Audit trail, temporal queries | Complex setup, eventual consistency |
| **CQRS** | Read/write optimization | Increased complexity, better performance |
| **Microservices** | Independent deployment | Network complexity, distributed challenges |
| **Domain-Driven Design** | Complex business domains | Learning curve, modeling overhead |

#### **Q9: How would you scale this system?**

**Performance Optimizations:**
```java
// Caching frequently accessed data
@Cacheable("available-spots")
public Map<ParkingSpotType, Integer> getAvailableSpots() {
    // Expensive database query cached
}

// Database indexing
@Entity
@Table(name = "parking_spots", 
       indexes = {@Index(columnList = "spot_type,is_available")})
public class ParkingSpot {
    // Optimized queries for spot availability
}

// Connection pooling
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

**Horizontal Scaling:**
- **Load Balancers**: Multiple application instances
- **Database Sharding**: Partition by location/floor
- **Caching Layer**: Redis for session management
- **Message Queues**: Async notification processing

#### **Q10: How does this implement Dependency Inversion?**

**High-level modules don't depend on low-level modules:**

```java
@Service
public class ParkingLotService {
    // Depends on abstraction (interface), not concretion
    @Autowired
    private PricingStrategy pricingStrategy;  // ✅ Good
    
    @Autowired
    private List<NotificationObserver> observers;  // ✅ Good
    
    // NOT depending on concrete implementations:
    // private TimeBasedPricingStrategy pricing;  // ❌ Bad
    // private EmailNotificationObserver emailObs; // ❌ Bad
}
```

**Benefits:**
- **Flexible**: Swap implementations without code changes
- **Testable**: Easy to inject mocks
- **Configurable**: Change behavior via configuration

---

## 🔮 Extension Ideas

### **🎯 System Design Interview Questions**

#### **Q1: How would you handle high traffic (1M+ vehicles/day)?**

**Bottlenecks & Solutions:**

```
Current System Limits:
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Single App    │───▶│   Single DB     │───▶│  Single Thread  │
│   Instance      │    │   Instance      │    │  Processing     │
└─────────────────┘    └─────────────────┘    └─────────────────┘

Scaled Solution:
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Load Balancer  │───▶│   App Cluster   │───▶│  Message Queue  │
│                 │    │ (3-5 instances) │    │   (RabbitMQ)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Redis Cache    │    │  DB Cluster     │    │ Notification    │
│  (Sessions)     │    │ (Read Replicas) │    │   Workers       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

**Implementation Strategy:**
- **Horizontal Scaling**: Multiple app instances behind load balancer
- **Database Optimization**: Read replicas, connection pooling, indexing
- **Caching**: Redis for frequently accessed data
- **Async Processing**: Message queues for notifications
- **CDN**: Static content delivery

#### **Q2: How to ensure data consistency in distributed system?**

**ACID vs BASE:**

```java
// ACID Approach (Strong Consistency)
@Transactional
public ParkingTicket parkVehicle(Vehicle vehicle) {
    ParkingSpot spot = spotRepository.findAndLockAvailableSpot(vehicle.getType());
    if (spot == null) throw new NoSpotsAvailableException();
    
    spot.occupy(vehicle);
    ParkingTicket ticket = createTicket(vehicle, spot);
    
    spotRepository.save(spot);
    ticketRepository.save(ticket);
    
    return ticket; // All or nothing
}

// BASE Approach (Eventual Consistency)
@EventDriven
public CompletableFuture<ParkingTicket> parkVehicleAsync(Vehicle vehicle) {
    return CompletableFuture
        .supplyAsync(() -> reserveSpot(vehicle))
        .thenCompose(spot -> createTicketAsync(vehicle, spot))
        .thenCompose(ticket -> notifySystemAsync(ticket))
        .exceptionally(this::handleParkingFailure);
}
```

#### **Q3: Design for multi-location parking lot chain?**

**Hierarchical Design:**

```
                    🏢 ParkingLotChain
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
   🏬 Location1        🏬 Location2       🏬 Location3
   (Downtown)          (Airport)          (Mall)
        │                  │                  │
   ┌────┼────┐        ┌────┼────┐        ┌────┼────┐
   │    │    │        │    │    │        │    │    │
 Floor1 F2  F3      F1   F2   F3       F1   F2   F3
```

**Code Structure:**
```java
@Entity
public class ParkingLotChain {
    @OneToMany(mappedBy = "chain")
    private List<ParkingLocation> locations;
    
    public ParkingSpot findBestSpot(VehicleType type, Location preference) {
        return locations.stream()
            .sorted(by(distance(preference)))
            .map(location -> location.findAvailableSpot(type))
            .filter(Objects::nonNull)
            .findFirst()
            .orElse(null);
    }
}

@Entity  
public class ParkingLocation {
    private String locationId;
    private Address address;
    private List<ParkingFloor> floors;
    
    @ManyToOne
    private ParkingLotChain chain;
}
```

#### **Q4: How to implement dynamic pricing based on demand?**

**Real-time Pricing Strategy:**

```java
@Component
public class DemandBasedPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(long duration, VehicleType type) {
        double basePrice = getBasePrice(type);
        double demandMultiplier = calculateDemandMultiplier();
        double timeMultiplier = getTimeMultiplier();
        
        return basePrice * duration * demandMultiplier * timeMultiplier;
    }
    
    private double calculateDemandMultiplier() {
        double occupancyRate = spotService.getOccupancyRate();
        
        if (occupancyRate > 0.9) return 2.0;      // 90%+ occupied: 2x price
        if (occupancyRate > 0.7) return 1.5;      // 70%+ occupied: 1.5x price
        if (occupancyRate > 0.5) return 1.2;      // 50%+ occupied: 1.2x price
        return 1.0;                               // Normal pricing
    }
    
    private double getTimeMultiplier() {
        LocalTime now = LocalTime.now();
        
        // Peak hours: 8-10 AM, 5-7 PM
        if (isPeakHour(now)) return 1.3;
        
        // Late night: 10 PM - 6 AM  
        if (isLateNight(now)) return 0.8;
        
        return 1.0; // Normal hours
    }
}
```

#### **Q5: How to implement reservation system with overbooking?**

**Airline-style Overbooking:**

```java
@Service
public class ReservationService {
    
    private static final double OVERBOOKING_FACTOR = 1.1; // 10% overbooking
    
    public Reservation reserveSpot(VehicleType type, LocalDateTime from, LocalDateTime to) {
        int availableSpots = getPhysicalSpots(type);
        int reservedSpots = getReservedSpots(type, from, to);
        int allowedReservations = (int) (availableSpots * OVERBOOKING_FACTOR);
        
        if (reservedSpots >= allowedReservations) {
            throw new NoReservationAvailableException();
        }
        
        return createReservation(type, from, to);
    }
    
    @Scheduled(fixedRate = 60000) // Every minute
    public void handleOverbookingSituation() {
        for (VehicleType type : VehicleType.values()) {
            int physical = getPhysicalSpots(type);
            int arriving = getArrivingReservations(type, LocalDateTime.now().plusHours(1));
            
            if (arriving > physical) {
                handleOverbooking(type, arriving - physical);
            }
        }
    }
    
    private void handleOverbooking(VehicleType type, int excessReservations) {
        // Strategy 1: Upgrade to higher tier (Motorcycle -> Car spot)
        // Strategy 2: Offer compensation (discount/refund)
        // Strategy 3: Redirect to nearby locations
        List<Reservation> affectedReservations = getFlexibleReservations(type, excessReservations);
        
        for (Reservation reservation : affectedReservations) {
            offerAlternatives(reservation);
        }
    }
}
```

### **Beginner Level**
1. **Add Vehicle Validation**: Enhanced license plate patterns
2. **Extend Vehicle Types**: ElectricCar, DisabledVehicle
3. **More Payment Methods**: UPI, Bitcoin, Loyalty Points
4. **Basic Reports**: Daily/Monthly revenue summaries

### **Intermediate Level**
1. **Reservation System**: Advanced booking with time slots
2. **Dynamic Pricing**: Peak hour multipliers, demand-based pricing
3. **Multi-location**: Chain of parking lots
4. **Mobile App Integration**: QR codes, push notifications

### **Advanced Level**
1. **Microservices Architecture**: Split into Payment, Notification, Parking services
2. **Event Sourcing**: Track all state changes as events
3. **Machine Learning**: Predict peak hours, optimize pricing
4. **IoT Integration**: Real sensor data from parking spots

### **Enterprise Level**
1. **CQRS Pattern**: Separate read/write models
2. **Kubernetes Deployment**: Container orchestration
3. **Monitoring & Observability**: Prometheus, Grafana, distributed tracing
4. **Security**: OAuth2, JWT, rate limiting

### **🧠 System Design Deep Dive**

#### **Parking Lot at Scale - Netflix/Google Level**

**Requirements:**
- **100M+ vehicles per day globally**
- **99.99% uptime** (52 minutes downtime/year)
- **Sub-100ms response times**
- **Global distribution** across 50+ countries
- **Real-time updates** for mobile apps

**Architecture Blueprint:**

```
                        🌍 Global Load Balancer (CloudFlare)
                                      │
                     ┌────────────────┼────────────────┐
                     │                │                │
                🌎 US Region      🌍 EU Region      🌏 APAC Region
                     │                │                │
            ┌────────┼────────┐       │        ┌───────┼───────┐
            │        │        │       │        │       │       │
         West-1   East-1   East-2  EU-West  APAC-1  APAC-2  APAC-3
            │        │        │       │        │       │       │
            ▼        ▼        ▼       ▼        ▼       ▼       ▼
    ┌─────────────────────────────────────────────────────────────┐
    │                 Microservices Mesh                         │
    │  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌──────────┐ │
    │  │   Auth     │ │  Vehicle   │ │  Parking   │ │ Payment  │ │
    │  │  Service   │ │  Service   │ │  Service   │ │ Service  │ │
    │  └────────────┘ └────────────┘ └────────────┘ └──────────┘ │
    │  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌──────────┐ │
    │  │Notification│ │ Analytics  │ │   Spot     │ │ Booking  │ │
    │  │  Service   │ │  Service   │ │  Service   │ │ Service  │ │
    │  └────────────┘ └────────────┘ └────────────┘ └──────────┘ │
    └─────────────────────────────────────────────────────────────┘
                                │
                                ▼
    ┌─────────────────────────────────────────────────────────────┐
    │                    Data Layer                               │
    │ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌─────┐ │
    │ │PostgreSQL    │ │    Redis     │ │ Elasticsearch│ │ S3  │ │
    │ │(Transactions)│ │   (Cache)    │ │  (Analytics) │ │Files│ │
    │ └──────────────┘ └──────────────┘ └──────────────┘ └─────┘ │
    │ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │
    │ │   Kafka      │ │   MongoDB    │ │  InfluxDB    │        │
    │ │(Event Stream)│ │  (Sessions)  │ │ (Metrics)    │        │
    │ └──────────────┘ └──────────────┘ └──────────────┘        │
    └─────────────────────────────────────────────────────────────┘
```

#### **Data Flow & Event Sourcing**

```java
// Event Sourcing Implementation
@Entity
public class ParkingEvent {
    private String eventId;
    private String aggregateId; // Vehicle license or spot ID
    private String eventType;   // VEHICLE_ENTERED, VEHICLE_EXITED, SPOT_RESERVED
    private LocalDateTime timestamp;
    private String eventData;   // JSON payload
    private Long version;       // For optimistic locking
}

@Service
public class EventStore {
    
    public void saveEvent(ParkingEvent event) {
        // Append-only event log
        eventRepository.save(event);
        
        // Publish to event stream
        kafkaTemplate.send("parking-events", event);
    }
    
    public ParkingSpotAggregate replayEvents(String spotId) {
        List<ParkingEvent> events = eventRepository.findByAggregateIdOrderByVersion(spotId);
        
        ParkingSpotAggregate aggregate = new ParkingSpotAggregate(spotId);
        events.forEach(aggregate::apply);
        
        return aggregate;
    }
}

// CQRS Read Model
@Service
public class ParkingSpotProjection {
    
    @EventHandler
    public void on(VehicleParkedEvent event) {
        // Update read model for fast queries
        spotReadModel.updateOccupancy(event.getSpotId(), true);
        
        // Update analytics
        analyticsService.recordParking(event);
        
        // Trigger notifications
        notificationService.notifyParked(event);
    }
}
```

#### **Performance Optimizations**

```java
// Database Sharding Strategy
@Configuration
public class ShardingConfiguration {
    
    @Bean
    public ShardingDataSource shardingDataSource() {
        // Shard by location hash
        ShardingRuleConfiguration shardingRule = new ShardingRuleConfiguration();
        
        // Table sharding: parking_spots_001, parking_spots_002, etc.
        TableRuleConfiguration spotTableRule = new TableRuleConfiguration("parking_spots");
        spotTableRule.setActualDataNodes("ds$->{0..3}.parking_spots_$->{001..999}");
        
        // Sharding algorithm: hash(location_id) % 4
        spotTableRule.setDatabaseShardingStrategyConfig(
            new StandardShardingStrategyConfiguration("location_id", new LocationShardingAlgorithm())
        );
        
        return ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRule, props);
    }
}

// Caching Strategy
@Service
@CacheConfig(cacheNames = "parking-cache")
public class CachedParkingService {
    
    @Cacheable(key = "#locationId + '_' + #vehicleType")
    public List<ParkingSpot> getAvailableSpots(String locationId, VehicleType vehicleType) {
        // Expensive DB query - cached for 30 seconds
        return spotRepository.findAvailableSpots(locationId, vehicleType);
    }
    
    @CacheEvict(key = "#spot.locationId + '_' + #spot.vehicleType")
    public void occupySpot(ParkingSpot spot, Vehicle vehicle) {
        // Invalidate cache when spot is occupied
        spot.occupy(vehicle);
        spotRepository.save(spot);
    }
}

// Rate Limiting
@RestController
@RateLimiter(name = "parking-api", fallbackMethod = "rateLimitFallback")
public class ParkingLotController {
    
    @PostMapping("/enter")
    @RateLimiter(name = "parking-enter", limit = "100/minute")
    public ResponseEntity<?> parkVehicle(@RequestBody ParkVehicleRequest request) {
        // Limited to 100 requests per minute per IP
        return ResponseEntity.ok(parkingService.parkVehicle(request));
    }
    
    public ResponseEntity<?> rateLimitFallback(Exception ex) {
        return ResponseEntity.status(429)
            .body(new ErrorResponse("Rate limit exceeded. Please try again later."));
    }
}
```

#### **Monitoring & Observability**

```yaml
# Prometheus Metrics
management:
  endpoints:
    web:
      exposure:
        include: "*"
  metrics:
    export:
      prometheus:
        enabled: true
    tags:
      application: parking-lot-service
      region: us-east-1

# Custom Metrics
@Component
public class ParkingMetrics {
    
    private final Counter parkedVehicles = Counter.builder("vehicles_parked_total")
        .description("Total vehicles parked")
        .tag("vehicle_type", "unknown")
        .register(Metrics.globalRegistry);
    
    private final Gauge occupancyRate = Gauge.builder("parking_occupancy_rate")
        .description("Current parking occupancy rate")
        .register(Metrics.globalRegistry, this, ParkingMetrics::calculateOccupancy);
    
    @EventListener
    public void onVehicleParked(VehicleParkedEvent event) {
        parkedVehicles.increment(Tags.of("vehicle_type", event.getVehicleType().toString()));
    }
}
```

#### **Security & Compliance**

```java
// OAuth2 + JWT Security
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/parking/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .build();
    }
}

// Data Encryption
@Entity
public class Vehicle {
    
    @Convert(converter = EncryptedStringConverter.class)
    private String licenseNumber; // PII encrypted at rest
    
    @JsonIgnore
    @Convert(converter = EncryptedStringConverter.class)
    private String ownerDetails; // GDPR compliance
}

// Audit Logging
@Aspect
@Component
public class AuditAspect {
    
    @Around("@annotation(Auditable)")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        String method = joinPoint.getSignature().getName();
        
        AuditLog log = new AuditLog(userId, method, LocalDateTime.now());
        
        try {
            Object result = joinPoint.proceed();
            log.setStatus("SUCCESS");
            return result;
        } catch (Exception e) {
            log.setStatus("FAILURE");
            log.setError(e.getMessage());
            throw e;
        } finally {
            auditRepository.save(log);
        }
    }
}
```

---

## 💡 Key Takeaways

### **What You've Learned**

1. **Design Patterns in Practice**: Not just theory, but real implementation
2. **SOLID Principles**: How they guide code structure decisions  
3. **OOP Mastery**: Inheritance, polymorphism, encapsulation, abstraction
4. **Spring Boot Proficiency**: Enterprise Java development
5. **Database Design**: JPA relationships and mapping strategies
6. **API Design**: RESTful services and proper HTTP usage

### **Industry Skills Gained**

- ✅ **Code Organization**: Layered architecture, package structure
- ✅ **Design Thinking**: When and why to use specific patterns
- ✅ **Testing Strategy**: Unit tests, integration tests, API testing
- ✅ **Documentation**: Comprehensive technical documentation
- ✅ **Problem Solving**: Real-world business logic implementation

---

## 🤝 Contributing

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/new-pattern`
3. **Implement** your changes with tests
4. **Document** your additions in this README
5. **Submit** a pull request

### **Contribution Ideas**
- Add new design patterns (Command, Mediator, Chain of Responsibility)
- Implement additional vehicle types
- Add comprehensive test coverage
- Create performance benchmarks
- Add Docker containerization

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Spring Boot Team**: For the amazing framework
- **Gang of Four**: For design pattern foundations  
- **Uncle Bob**: For SOLID principles
- **Martin Fowler**: For enterprise architecture patterns

---

## 📞 Support & Questions

- **Issues**: Open GitHub issues for bugs or questions
- **Discussions**: Use GitHub Discussions for architectural questions
- **Documentation**: This README covers most scenarios

**Happy Learning! 🚀**

---

*This project is designed to be a comprehensive learning resource. Take your time to explore each component, understand the design decisions, and experiment with extensions. The best way to learn is by doing!*
