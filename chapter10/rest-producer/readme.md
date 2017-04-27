chapter10-rest-producer
-----------------------

A little example to show how to use Camel's new rest component to call an existing REST service.

The REST service is not external but embedded in the same JVM using Spring REST.

The Camel route `RestProducerRoute` triggers to run every 5th second and call the Spring REST service.

### 10.2.6 Calling RESTful services using Rest DSL

To run this example

    mvn spring-boot:run
    
And look in the console log the responses.
    
You can also call the servie from a web browser to find the country of your city, eg 
    
    http://localhost:8080/country/{city}
    
For example
    
    http://localhost:8080/country/Madrid
        
        
    