chapter7-prototype2-rating
--------------------------

SaaS rating microservice.

This implementation does not call an online SaaS provider 
but is a mere Spring Boot application to simulate a SaaS service.

You can run this service using:

    mvn spring-boot:run
    
This microservice is running Spring Boot with Camel exposing a REST service at:
    
    http://localhost:8383/api/ratings

For example to get ratings of item 123 and 456

    http://localhost:8383/api/ratings/123,456
      
**Note** this implementation uses Camels rest-dsl which we will cover extensively in chapter 10.

