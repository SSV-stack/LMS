@echo off
echo Starting LMS Services...

start "Eureka Server" cmd /k "cd eureka-server && mvn spring-boot:run"
timeout /t 10

start "API Gateway" cmd /k "cd api-gateway && mvn spring-boot:run"
timeout /t 5

start "Auth Service" cmd /k "cd auth-service && mvn spring-boot:run"
start "Course Service" cmd /k "cd course-service && mvn spring-boot:run"
start "Content Service" cmd /k "cd content-service && mvn spring-boot:run"
start "IDE Service" cmd /k "cd ide-service && mvn spring-boot:run"
start "Git Service" cmd /k "cd git-service && mvn spring-boot:run"
start "AI Service" cmd /k "cd ai-service && mvn spring-boot:run"
start "Notification Service" cmd /k "cd notification-service && mvn spring-boot:run"

echo All services are starting...
echo Please wait for them to initialize.
