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

   {
     "id": 2526551,
     "login": "olafos",
     "name": "Olaf Tomczak",
     "type": "User",
     "avatarUrl": "https://avatars.githubusercontent.com/u/2526551?v=4",
     "createdAt": "2012-10-10T07:59:36Z",
     "calculations": 84
   }

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
