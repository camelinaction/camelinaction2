chapter20 - vertx-camel2
------------------------

Using vert.x and Apache Camel to build a microservice which simulates a football
live score ticker which authentic _bell_ sound when goals are scored.

#### Note 

This example uses the `vertx-camel-bridge` component which requires setting up
the bridge using vert.x style with inbound and outbound mappings.
The Camel route then need to use seda endpoints to route between vert.x and Camel.

### 21.2.2 Using Camel together with Vert.x

You need to build this example first

    mvn install
    
And then you can run the example using
    
    mvn vertx:run
    
And from a web browser open the following url:

    http://localhost:8080/

You can also run the example as a fat jar using: 

    java -jar target/livescore.jar

