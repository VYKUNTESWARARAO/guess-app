# Stage 1: Build the Spring Boot app
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom and source code
COPY pom.xml . 
COPY src ./src

# Build WAR
RUN mvn clean package -DskipTests

# Stage 2: Run the app
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Set environment variable to avoid Netty native SSL crash
ENV GRPC_NETTY_USE_NATIVE_TRANSPORT=false
ENV FIREBASE_SERVICE_ACCOUNT=/app/firebase-service-account.json

# Copy WAR file
COPY --from=build /app/target/gts-0.0.1-SNAPSHOT.war app.war

# Expose your port
EXPOSE 4545

# Create Firebase JSON file from environment variable at runtime
RUN echo "$FIREBASE_SERVICE_ACCOUNT_JSON" > /app/firebase-service-account.json

# Run the app
ENTRYPOINT ["java","-jar","app.war"]
