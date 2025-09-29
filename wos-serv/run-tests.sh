#!/bin/bash

# Test runner script for BeastSlayTask tests
# This script demonstrates how to run the tests for the BeastSlayTask class

echo "🧪 Running BeastSlayTask Tests"
echo "================================"

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed or not in PATH"
    echo "Please install Maven to run the tests"
    exit 1
fi

# Navigate to the wos-serv directory
cd "$(dirname "$0")"

echo "📁 Current directory: $(pwd)"
echo ""

# Run the specific test class
echo "🚀 Running BeastSlayTaskTest..."
mvn test -Dtest=BeastSlayTaskTest

# Check if tests passed
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ All tests passed successfully!"
    echo ""
    echo "📊 Test Summary:"
    echo "- Static method tests: calculateFullStaminaTime"
    echo "- Private method tests: extractFirstNumber (via reflection)"
    echo "- Main execution tests: execute method with mocked dependencies"
    echo "- Edge case tests: OCR exceptions, low stamina scenarios"
    echo "- Integration tests: constructor and required start location"
else
    echo ""
    echo "❌ Some tests failed. Check the output above for details."
    exit 1
fi
