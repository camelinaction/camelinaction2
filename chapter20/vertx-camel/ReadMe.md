chapter20 - vertx-camel
----------------------

Using vert.x and Apache Camel to build a microservice which simulates a football
live score ticker which authentic _bell_ sound when goals are scored.

#### Note 

This example uses the `camel-vertx` component which uses a regular Camel
component to route between Vert.x and Camel.

### 20.2.2 Using Camel together with Vert.x

You need to build this example first

    mvn install
    
And then you can run the example using
    
    mvn vertx:run
    
And from a web browser open the following url:

    http://localhost:8080/

You can also run the example as a fat jar using: 

    java -jar target/livescore.jar

