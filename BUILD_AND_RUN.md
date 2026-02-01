# 🚀 Build and Run Guide - LMS Project

This guide shows how to build and run the Learning Management System locally without Docker.

## 📋 Prerequisites

### Required Software
- **Java 17+** (OpenJDK or Oracle JDK)
- **Maven 3.8+**
- **Node.js 18+** and **npm** (for frontend)
- **Git**

### Verify Installation
```bash
java -version
mvn -version
node --version
npm --version
```

## 🗄️ Database Setup

### H2 Database Configuration

This project uses **H2 in-memory databases** for easy local development. No external database installation is required!

Each service has its own H2 database:
- **Auth Service**: `lms_auth_db`
- **Course Service**: `lms_courses_db`
- **IDE Service**: `lms_ide_db`

### Access H2 Console

Each service provides a web-based H2 console for debugging:
- **Auth Service**: http://localhost:8081/h2-console
- **Course Service**: http://localhost:8082/h2-console
- **IDE Service**: http://localhost:8085/h2-console

**Console Login Details:**
- **JDBC URL**: `jdbc:h2:mem:lms_auth_db` (or respective database)
- **User Name**: `sa`
- **Password**: (leave empty)
- **Driver Class**: `org.h2.Driver`

### Database Schema

The databases are automatically created and managed by Spring Boot JPA with `ddl-auto: create-drop`, which means:
- Tables are created automatically on startup
- Data is reset when services restart
- Perfect for development and testing

## 🔧 Build the Project

### 1. Build All Modules
From the project root directory:

```bash
# Clean and compile all modules
mvn clean compile

# Build all modules
mvn clean install -DskipTests
```

### 2. Individual Module Build (Optional)
```bash
# Build common module first
cd common
mvn clean install

# Build other modules
cd ../auth-service
mvn clean install

cd ../course-service
mvn clean install

cd ../ide-service
mvn clean install

cd ../api-gateway
mvn clean install
```

## 🚀 Run the Services

### Option 1: Manual Startup (Recommended for Development)

#### 1. Start Services (No Database Required)
Since we're using H2 in-memory databases, no external database setup is needed!

#### 2. Start Services in Order
Open separate terminal windows for each service:

**Terminal 1 - API Gateway:**
```bash
cd api-gateway
mvn spring-boot:run
```

**Terminal 2 - Auth Service:**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 3 - Course Service:**
```bash
cd course-service
mvn spring-boot:run
```

**Terminal 4 - IDE Service:**
```bash
cd ide-service
mvn spring-boot:run
```

#### 3. Start Frontend (New Terminal)
```bash
cd frontend
npm install
npm start
```

### Option 2: Using Maven Multi-Module

#### 1. Create Eureka Server (Service Discovery)
First, let's create the missing Eureka server:

```bash
# Create eureka-server directory
mkdir eureka-server
cd eureka-server
```

Create `eureka-server/pom.xml` and the necessary Java files (see below).

#### 2. Run with Maven
From the root directory:
```bash
# Start all services (requires proper configuration)
mvn spring-boot:run -pl api-gateway
mvn spring-boot:run -pl auth-service
mvn spring-boot:run -pl course-service
mvn spring-boot:run -pl ide-service
```

## 🌐 Access Points

Once all services are running:

- **API Gateway**: http://localhost:8080
- **Auth Service**: http://localhost:8081
- **Course Service**: http://localhost:8082
- **IDE Service**: http://localhost:8085
- **Frontend**: http://localhost:3000

## 🧪 Test the Setup

### 1. Test API Gateway
```bash
curl http://localhost:8080/actuator/health
```

### 2. Test Auth Service
```bash
# Register a user
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'

# Login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 3. Test Course Service
```bash
# Get all courses
curl http://localhost:8082/api/courses
```

## 🔧 Configuration Notes

### Environment Variables
You can set these environment variables to override defaults:

```bash
# JWT settings
export JWT_SECRET=your-secret-key

# Gold-IDE settings
export GOLD_IDE_BASE_URL=https://your-gold-ide.com
export GOLD_IDE_API_KEY=your-api-key
```

### Port Conflicts
If ports are already in use, update them in the respective `application.yml` files:

```yaml
server:
  port: 8081  # Change to available port
```

## 🐛 Common Issues & Solutions

### 1. Database Connection Issues
- H2 databases are created automatically
- Check service logs for any database errors
- Ensure no port conflicts between services

### 2. Port Already in Use
```bash
# Find process using port
netstat -ano | findstr :8080  # Windows
lsof -i :8080  # macOS/Linux

# Kill process if needed
taskkill /PID <PID> /F  # Windows
kill -9 <PID>  # macOS/Linux
```

### 3. Maven Build Failures
- Clean and rebuild: `mvn clean install`
- Check Java version: `java -version`
- Verify Maven version: `mvn -version`

### 4. Frontend Issues
```bash
# Clear npm cache
npm cache clean --force

# Delete node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

## 📝 Development Tips

### 1. Hot Reload
- Backend services support hot reload with Spring DevTools
- Frontend supports hot reload with Create React App

### 2. Debugging
- Add JVM args for debugging: `-Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"`
- Use IDE debuggers to connect to the specified port

### 3. Logging
- Check logs in each service's console output
- Adjust log levels in `application.yml`:
```yaml
logging:
  level:
    in.maven.ark.lms: DEBUG
```

## 🚀 Next Steps

1. **Create Eureka Server** for service discovery
2. **Set up Frontend** with proper routing
3. **Configure Gold-IDE** integration
4. **Add Content Service** for media handling
5. **Implement Git Service** for repository management
6. **Create AI Service** for intelligent features

## 📞 Support

If you encounter issues:
1. Check the console logs for each service
2. Verify database connections
3. Ensure all prerequisites are installed
4. Check for port conflicts
