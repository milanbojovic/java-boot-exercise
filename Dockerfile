FROM openjdk:8-jdk as EXECUTOR

# Set base directory
WORKDIR /java-boot-exercise

# Copy source code to container
COPY . ./

#Execute gradle compile run deploy
CMD ./gradlew bootRun

# Make port 8080 available to outside world
EXPOSE 8080
