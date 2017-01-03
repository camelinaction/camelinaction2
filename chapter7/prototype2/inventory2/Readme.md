chapter7-prototype2-inventory
-----------------------------

Inventory backend system.

You can run this service using:

    mvn compile exec:java
    
This application is running using standalone Camel application an exposes a JMS endpoint 
    
    tcp://localhost:61616

**Note** this implementation embeds an ActiveMQ broker to simulate a legacy system which can only be accessed using JMS messaging.

