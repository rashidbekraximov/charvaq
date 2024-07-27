# First stage: Build the application using Maven
FROM maven:3.8.6-openjdk-11-slim AS build

# Set the working directory for the build
WORKDIR /build

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Second stage: Create the final image with JRE
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Create a directory for images
RUN mkdir -p /opt/images

COPY default.jpg /opt/images/default.jpg

# Copy the jar file from the first stage
COPY --from=build /build/target/charvaq-0.0.1.jar charvaq.jar

# Expose the port that the application will run on
EXPOSE 3030

# Run the jar file
ENTRYPOINT ["java", "-jar", "charvaq.jar"]
