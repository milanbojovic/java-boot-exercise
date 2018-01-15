#Phase I [VCS] - Fetch code from github
FROM alpine/git as VCS
WORKDIR /tmp
RUN git clone https://github.com/milanbojovic/java-boot-exercise.git

#Phase II [BUILDER] - Use Gradle for running the app
FROM gradle as BUILDER
WORKDIR /code
# Copy source code from PHASE I into current work dir
COPY --from=VCS /tmp/java-boot-exercise/ .
RUN ./gradlew bootRun

# Make ports available to the world outside this container
EXPOSE 8080 8080
