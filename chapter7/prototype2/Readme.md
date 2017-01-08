chapter7 - prototype2
=====================

This is the 2nd prototype for the Rider Auto Parts Recommendation system.
The prototype is implemented as a set of microservices:

- cart - Shopping Cart service
- inventory - Inventory backend service
- rating - SaaS rating service
- rules - Rules engine service
- recommend - Recommendation service

Each microservice is implemented using different technology stack because you want to get familiar with these different technologies.

For this 2nd prototype the microservices are designed with fault tolerance using Netflix Hystrix circuit breakers and Camel error handling.

To run this prototype you need to start all the microservices from their own command shell:

    cd cart2
    mvn compile exec:java
    
    cd inventory2
    mvn compile exec:java
    
    cd rating2
    mvn spring-boot:run
    
    cd rules2
    mvn wildfly-swarm:run
    
    cd recommend2
    mvn spring-boot:run
    
Then you can open a web browser and open `http://localhost:8080/api/recommend`

Because the microservices are fault tolerant, you should be able to stop individual microservices
and have the system continue running and deal with those failures in a graceful way using fallbacks.

For example you can stop the rating system, and all recommendations are giving a default rating.
If you stop the inventory system, then only a limited and known set of inventories are applicable for recommendation.
