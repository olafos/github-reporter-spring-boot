# github-reporter-spring-boot

## Overview

REST API implemented using Spring Boot and MongoDB.

This is an attempt to employ [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/) to
build a simple application with REST interface.

Project is divided into 3 packages:
* core - contains domain model, use cases and ports and is completely framework and DB agnostic
* adapters - port implementations
* config - Spring container configuration

## Getting Started

Application requires:
* Java 17+
* Gradle
* running Mongo DB server (on localhost:27017 by default).

1. Build application and run tests:
   
   ```bash
   ./gradlew build
   ```
3. Start application:
   
   ```bash
   ./gradlew bootRun
   ```
4. Experiment with the API:

   ```bash
   curl http://localhost:8080/users/olafos | jq
   ```
5. Check request count

   ```bash
   mongo
   > use github-report-metrics
   switched to github-report-metrics
   > db.request_count.find()
   { "_id" : "olafos", "LOGIN" : "olafos", "REQUEST_COUNT" : NumberLong(11) }
   { "_id" : "octocat", "LOGIN" : "octocat", "REQUEST_COUNT" : NumberLong(9) }
   ```

Configuration options (like Mongo DB connection) can be tweaked in `src/[main|test]/resources/application.yaml`
