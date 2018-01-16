# Dockerfile was added for this project
Docker commands for building and running app can be found below
```bash
docker build -t java-boot-exercise-image .
docker run -d --restart=always -p 8080:8080 java-boot-exercise-image
```

# Java Coding Exercise

The city of Gotham has decided to perform an analysis on the noise of its street. Several sensors have been put at different parts of the city which register the current noise (in decibels) every minute. Your task is to write a REST API which stores data received by this sensors and provides some basic statistical data.

- Create a POST endpoint which sensors will call every minute with the following input, and stores data in a database
  
  Inputs:
  ```
    id: <sensor id>
    value: <current noise level in db>
    timestamp: <date and time when this measurement was taken>
  ```
  
- Create a Scheduler which runs once every hour and calculates for each sensor the median noise level for that hour. This data needs to be stored in the database as well.

- Create a GET endpoint which returns for a given sensor id the list of the median noise levels for a given period. Here a sample input/output
  
  Inputs:
  ```
    - id: <sensor id>
    - start: <period start timestamp>
    - end: <period end timestamp>
   ```

  Output:
  ```
  [
    {
      value: <median noise level in db>
      timestamp: <start timestamp for this hourly median>
    },
    {
      value: <median noise level in db>
      timestamp: <start timestamp for this hourly median>
    },
    ...
  ]
  ```

# Result
This project provides a running baseline with a Spring Boot, Gradle, JOOQ, Flyway and H2 setup (similar to what we use in production). It provides a REST endpoint for adding new sensors and an integration test. Take some time to understand it and use it as a starting point.

Please provide a Git repository with your code and the whole commit history as we are more interested in your approach rather than the end result. 

Codingwise just act the same as if you would be working in a professional environment with a team, focusing on clean and maintainable code. 

# Running and developing the example application

Run the application
```
./gradlew bootRun
``` 

Run tests
```
./gradlew test
``` 

Generate JOOQ classes from current db schema (in build/generated/jooq):
```
./gradlew generateDbJooqSchemaSource
``` 
