chapter7 - prototype
====================

This is a prototype for the Rider Auto Parts Recommendation system.
The prototype is implemented as a set of microservices:

- cart - Shopping Cart service
- inventory - Inventory backend service
- rating - SaaS rating service
- rules - Rules engine service
- recommend - Recommendation service

Each microservice is implemented using different technology stack because you want to get familiar with these different technologies.

To run this prototype you need to start all the microservices from their own command shell:

    cd cart
    mvn compile exec:java
    
    cd inventory
    mvn compile exec:java
    
    cd rating
    mvn spring-boot:run
    
    cd rules
    mvn wildfly-swarm:run
    
    cd recommend
    mvn spring-boot:run
    
Then you can open a web browser and open `http://localhost:8080/api/recommend`

