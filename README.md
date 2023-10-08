# Music Metadata Service

This repository contains a music metadata service spring boot application that stores and provides metadata about
different music tracks and artists.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
    - [Clone the Repository](#clone-the-repository)
    - [Build the Application](#build-the-application)
    - [Run the Application](#run-the-application)
    - [Test the Application](#test-the-application)
- [Documentation](#documentation)
- [Future Improvements](#future-improvements)

## Prerequisites

Before you begin, ensure you have the following prerequisites installed:

- Java Development Kit (JDK) 17 or higher
- Gradle (You can use the Gradle Wrapper provided in this repository)
- Git (for cloning the repository)
- Docker (for running the tests and provided docker compose configuration)

## Getting Started

### Clone the Repository

Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/vstasinopoulos/music-metadata-service.git
cd music-metadata-service
```

### Build the Application

To build the application, run the following command:

```bash
./gradlew clean build --refresh-dependencies
```

### Run the Application

To run the application as native java app, run the following commands:

```bash
docker compose -f docker-compose-db-only.yaml up -d
./gradlew bR
```

To run the application as dockerized service, run the following commands:

```bash
docker compose up -d
```

In both cases, the application will start, and you can access it at http://localhost:8081.

### Test the Application

To run the integration tests, run the following command:

```bash
./gradlew test
```

## Documentation

The API specs for the application is available at http://localhost:8081/swagger-ui.html.

## Future Improvements

- Apply authentication and authorisation using spring security
- Add more integration tests (happy path and error scenarios)
- Harmonize and improve error handling
- Improve logging
- Extract API components in a library
- Apply distributed locking when creating the artist of the day entity
- Consider keyset scroll paging instead of normal paging for performance improvement
- Clarify with business what would happen in the corner case where an artist in never presented as artist of the day and
  refactor code afterward.