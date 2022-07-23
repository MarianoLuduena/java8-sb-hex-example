# Hexagonal Architecture example in Java 8 with Spring Boot

Example adapted from [Buckpal](https://github.com/thombergs/buckpal) by Tom Hombergs.

The code revolves around the use case of transferring money from one account to another.

## Requirements

- [JDK 8](https://adoptium.net/es/temurin/releases)

## Getting started

Start application with `SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run`.

Run tests with `./mvnw test`.

## Package architecture

This project has been designed by taking the principles of clean architecture into account. More specifically it uses 
a variety known as Hexagonal Architecture (also known as "Ports and Adapter"). More information can be found in the
resources linked below:

- [Clean Architecture by Bob Martin](https://blog.cleancoder.com/uncle-bob/2011/11/22/Clean-Architecture.html)
- [The Clean Architecture by Bob Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Clean Micro-Service Architecture by Bob Martin](https://blog.cleancoder.com/uncle-bob/2014/10/01/CleanMicroserviceArchitecture.html)
- [Buckpal: Example implementation of a Hexagonal Architecture](https://github.com/thombergs/buckpal)

## Health check probe

`curl -fsS http://localhost:8080/actuator/health`
