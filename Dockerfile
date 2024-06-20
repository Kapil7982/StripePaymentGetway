# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY PortOne/Stripe /app
RUN mvn clean package -DskipTests

# Stage 2: Create the final image for deployment
FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/PortOne-0.0.1-SNAPSHOT.jar /app/Stripe.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "Stripe.jar"]
