chapter7-prototype2-recommend
-----------------------------

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

Because the microservices are fault tolerant, you should be able to stop individual microservices and have the system continue running and deal with those failures in a graceful way using fallbacks.

For example you can stop the rating system, and all recommendations are giving a default rating.

If you stop the inventory system, then only a limited and known set of inventories are applicable for recommendation.
