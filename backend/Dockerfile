# ==========================================
# Stage 1: Build Stage
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy Maven wrapper and pom.xml first to leverage Docker layer caching
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd pom.xml ./

# Grant execution permission to Maven wrapper script
RUN chmod +x mvnw

# Copy application source code
COPY src ./src

# Build production executable JAR (skipping unit tests)
RUN ./mvnw clean package -DskipTests

# ==========================================
# Stage 2: Production Runtime Stage
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Default fallback port if PORT is not set by environment (Render sets PORT dynamically)
ENV PORT=8080
EXPOSE 8080

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/photography-backend-1.0.0.jar app.jar

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
