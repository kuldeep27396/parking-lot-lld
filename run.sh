#!/bin/bash

# Parking Lot System - Build and Run Script
# This script helps you build and run the Spring Boot application

echo "🚗 Parking Lot System - Build and Run Script"
echo "=============================================="

# Check for Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "📝 Please install Java 17 or later:"
    echo "   • Download from: https://adoptium.net/"
    echo "   • Or use Homebrew: brew install openjdk@17"
    echo ""
    exit 1
fi

# Check for Maven
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed or not in PATH"
    echo "📝 Please install Maven:"
    echo "   • Download from: https://maven.apache.org/download.cgi"
    echo "   • Or use Homebrew: brew install maven"
    echo ""
    exit 1
fi

echo "✅ Java version:"
java -version

echo ""
echo "✅ Maven version:"
mvn -version

echo ""
echo "🔨 Building the project..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🚀 Running the application..."
    mvn spring-boot:run
else
    echo "❌ Build failed!"
    exit 1
fi
