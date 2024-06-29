#Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Create a directory for logs
RUN mkdir -p /opt/images

COPY default.jpg /opt/images/default.jpg

# Add the jar file
COPY target/charvaq-0.0.1.jar charvaq.jar

# Expose the port that the application will run on
EXPOSE 3030

# Run the jar file
ENTRYPOINT ["java", "-jar", "charvaq.jar"]