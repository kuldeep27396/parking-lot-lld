#!/bin/bash

echo "🚗 Testing Parking Lot System APIs"
echo "===================================="

BASE_URL="http://localhost:8080/parking-lot"

echo ""
echo "1. 🏗️ Get Parking Lot Status"
curl -s "$BASE_URL/api/v1/parking/status" | jq '.'

echo ""
echo ""
echo "2. 🚙 Park a Car"
curl -s -X POST "$BASE_URL/api/v1/parking/park" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseNumber": "ABC123",
    "vehicleType": "CAR",
    "color": "Red",
    "model": "Honda Civic",
    "customerPhone": "+1234567890"
  }' | jq '.'

echo ""
echo ""
echo "3. 🏍️ Park a Motorcycle" 
curl -s -X POST "$BASE_URL/api/v1/parking/park" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseNumber": "BIKE456",
    "vehicleType": "MOTORCYCLE", 
    "color": "Black",
    "model": "Yamaha R1",
    "customerPhone": "+1234567891"
  }' | jq '.'

echo ""
echo ""
echo "4. 🚛 Park a Truck"
curl -s -X POST "$BASE_URL/api/v1/parking/park" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseNumber": "TRUCK789",
    "vehicleType": "TRUCK",
    "color": "White", 
    "model": "Ford F-150",
    "customerPhone": "+1234567892"
  }' | jq '.'

echo ""
echo ""
echo "5. 📊 Check Updated Status"
curl -s "$BASE_URL/api/v1/parking/status" | jq '.'

echo ""
echo ""
echo "6. 🚗 Exit Vehicle"
curl -s -X POST "$BASE_URL/api/v1/parking/exit" \
  -H "Content-Type: application/json" \
  -d '{
    "licenseNumber": "ABC123",
    "paymentMethod": "CREDIT_CARD"
  }' | jq '.'

echo ""
echo ""
echo "7. 📊 Final Status Check"
curl -s "$BASE_URL/api/v1/parking/status" | jq '.'

echo ""
echo ""
echo "✅ API Testing Complete!"
