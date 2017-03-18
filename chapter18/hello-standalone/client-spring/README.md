Chapter 18 - client-spring-standalone
-------------------------------------

This holds the standalone version of the hello microservice.

### 18.1.1 Building and running Camel microservices locally

At first you need to run the WildFly-Swarm service which hosts the hello service,
which is located in the `hello-swarm` directory.

When WildFly-Swarm is up and running you can run this Spring-Boot client by running

    mvn spring-boot:run
    
Which should call the WildFly-Swarm service and log its reponse.

