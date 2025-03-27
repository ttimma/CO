# Use a lightweight Java runtime image
FROM openjdk:21-jdk-slim

# Create a working directory in the container
WORKDIR /app

# Copy the JAR file from your build to the container
COPY build/libs/Uptime-0.0.1-SNAPSHOT.jar /app/myapp.jar

# Expose the port your app runs on (Spring Boot default = 8080)
EXPOSE 8080

# Run the JAR
CMD ["java", "-jar", "/app/myapp.jar"]
