chapter7-prototype-recommend
----------------------------

Recommend microservice.

This microservice is a prototype of the Rider Auto Parts recommendation system.

This service uses the following microservices, which is required to be running:

- cart - Shopping Cart service
- inventory - Inventory backend service
- rating - SaaS rating service
- rules - Rules engine service

You can run this service using:

    mvn spring-boot:run
    
This microservice is running Spring Boot with Camel exposing a REST service at:
    
    http://localhost:8080/api/recommend

**Note:** This implementation is a vanilla Spring Boot application (without Camel)

