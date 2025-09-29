# Use a Maven image to build the project
FROM maven:3.9.0-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom and source code
COPY pom.xml .
COPY src ./src

# Package the Spring Boot app
RUN mvn clean package -DskipTests

# Use a lightweight JDK image for running
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/target/gts-0.0.1-SNAPSHOT.war app.war

# Expose port 8080
EXPOSE 4545

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.war"]
