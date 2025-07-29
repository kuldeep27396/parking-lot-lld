#!/usr/bin/env python3
import re
import os

# Quick script to add getters to DTO classes
controller_file = "src/main/java/com/lld/parkinglot/controller/ParkingLotController.java"

# Read the file
with open(controller_file, 'r') as f:
    content = f.read()

# Add getters to ExitVehicleRequest
content = re.sub(
    r'(public static class ExitVehicleRequest \{\s*private String ticketNumber;\s*)\}',
    r'\1\n        public String getTicketNumber() { return ticketNumber; }\n        public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }\n    }',
    content,
    flags=re.DOTALL
)

# Add getters to ReserveSpotRequest
content = re.sub(
    r'(public static class ReserveSpotRequest \{\s*private String spotNumber;\s*private int durationMinutes;\s*)\}',
    r'\1\n        public String getSpotNumber() { return spotNumber; }\n        public int getDurationMinutes() { return durationMinutes; }\n        public void setSpotNumber(String spotNumber) { this.spotNumber = spotNumber; }\n        public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }\n    }',
    content,
    flags=re.DOTALL
)

# Fix ParkingLotStatus constructor
content = re.sub(
    r'public static class ParkingLotStatus \{[^}]*\}',
    '''public static class ParkingLotStatus {
        private Map<ParkingSpotType, Long> availableSpots;
        private double occupancyRate;
        private int activeTickets;
        
        public ParkingLotStatus(Map<ParkingSpotType, Long> availableSpots, double occupancyRate, int activeTickets) {
            this.availableSpots = availableSpots;
            this.occupancyRate = occupancyRate;
            this.activeTickets = activeTickets;
        }
        
        public Map<ParkingSpotType, Long> getAvailableSpots() { return availableSpots; }
        public double getOccupancyRate() { return occupancyRate; }
        public int getActiveTickets() { return activeTickets; }
    }''',
    content,
    flags=re.DOTALL
)

# Write back
with open(controller_file, 'w') as f:
    f.write(content)

print("Added getters to controller DTO classes")
