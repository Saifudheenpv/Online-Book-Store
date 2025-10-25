# =========================
# Stage 1: Build
# =========================
FROM maven:3.9.3-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar (skip tests for faster builds)
RUN mvn clean package -DskipTests

# =========================
# Stage 2: Run
# =========================
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/online-book-store-1.0.0.jar ./app.jar

# Expose Spring Boot port
EXPOSE 8080

# Run the application on port 8081
ENV SERVER_PORT=8081
ENTRYPOINT ["java","-jar","app.jar"]
