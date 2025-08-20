# Use OpenJDK 21 slim
FROM openjdk:21-slim

# Set working directory
WORKDIR /app

# Copy the Spring Boot jar
COPY target/authService-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]