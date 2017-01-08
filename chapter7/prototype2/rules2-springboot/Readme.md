chapter7-prototype2-rules-springboot
------------------------------------

## 7.4.9 Using Camel Hystrix with Spring Boot

Rules Engine microservice.

This service is simulating a rules engine used by the recommendation service to rank
and filter which items to recommend to users whom are shopping the Rider Auto Parts website.

This example is using Spring Boot instead of WildFly Swarm as the original rules service.

You can run this service using:

    mvn spring-boot:run
    
This microservice is running Spring Boot with Camel exposing a REST service at:
    
    http://localhost:8181/api/rules

For example to get rules of item 123 and 456

    http://localhost:8181/api/rules/123,456
      
This application is implemented using Spring REST to expose the REST service
and then calling a Camel route which then integrates with a backend legacy inventory system using JMS.

