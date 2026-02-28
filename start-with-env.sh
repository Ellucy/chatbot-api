#!/bin/bash

# Load environment variables from .env file and start the application

if [ ! -f .env ]; then
    echo "Error: .env file not found!"
    echo "Create a .env file with: OPENAI_API_KEY=sk-your-key-here"
    exit 1
fi

echo "🔐 Loading environment variables from .env..."
set -a
source .env
set +a

echo "✅ Environment loaded"
echo "Starting Spring Boot application..."
./mvnw spring-boot:run
