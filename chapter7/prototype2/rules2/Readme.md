chapter7-prototype-rules
------------------------

Rules Engine microservice.

This service is simulating a rules engine used by the recommendation service to rank
and filter which items to recommend to users whom are shopping the Rider Auto Parts website.

You can run this service using:

    mvn wildfly-swarm:run
    
This microservice is running WildFly-Swarm with Camel exposing a REST service at:
    
    http://localhost:8181/api/rules

For example to get rules of item 123 and 456

    http://localhost:8181/api/rules/123,456
      
This application is implemented using JAX-RS to expose the REST service
and then calling a Camel route which then integrates with a backend legacy inventory system using JMS.

**Note:** This implementation using JEE APIs such as JAX-RS to expose the REST service.

