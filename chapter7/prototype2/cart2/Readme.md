chapter7-prototype2-cart
------------------------

Shopping cart microservice.

You can run this service using:

    mvn compile exec:java
    
This microservice is running using standalone Camel application that exposes a REST service on
    
    http://localhost:8282/api/cart
    
The service exposes a swagger api documentation you can access at:
    
    http://localhost:8282/api/api-doc
    
**Note** this implementation uses Camels rest-dsl which we will cover extensively in chapter 10.

