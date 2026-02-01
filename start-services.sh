#!/bin/bash
echo "Starting LMS Services..."

# Function to start a service in a new terminal window
start_service() {
    service_name=$1
    service_dir=$2
    if [[ "$OSTYPE" == "msys" || "$OSTYPE" == "cygwin" ]]; then
        start "$service_name" cmd /k "cd $service_dir && mvn spring-boot:run"
    elif [[ "$OSTYPE" == "darwin"* ]]; then
        osascript -e "tell application \"Terminal\" to do script \"cd $(pwd)/$service_dir && mvn spring-boot:run\""
    else
        x-terminal-emulator -e "bash -c 'cd $service_dir && mvn spring-boot:run; exec bash'" &
    fi
}

start_service "Eureka Server" "eureka-server"
sleep 15

start_service "API Gateway" "api-gateway"
sleep 10

start_service "Auth Service" "auth-service"
start_service "Course Service" "course-service"
start_service "Content Service" "content-service"
start_service "IDE Service" "ide-service"
start_service "Git Service" "git-service"
start_service "AI Service" "ai-service"
start_service "Notification Service" "notification-service"

echo "All services are starting..."
