chapter7 - spring-boot-camel
----------------------------

Running Spring Boot and using Camel route to define REST service using Servlet

### 7.2.4  Spring Boot with Camel as microservice 

You need to build this example first

    mvn install
    
And then you can run the example using
    
    mvn spring-boot:run
    
And from a web browser open the following url:

    http://localhost:8080/camel/hello

You can also run the example as a fat-jar using: 

    java -jar target/chapter7-springboot-camel-2.0.0.jar
    
Notice this example uses online internet access to lookup your current location.
This is done using the camel-geocoder component.

