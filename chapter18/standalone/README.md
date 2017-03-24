Chapter 18 - standalone
-----------------------

This holds the standalone version of the hello microservice.

### 18.1.1 Building and running Camel microservices locally

At first you need to run the WildFly-Swarm service which hosts the hello service 
that the Spring-Boot client will call.

    cd hello-swarm
    mvn wildfly-swarm:run
    
And then you can start the client 

    cd client-spring
    mvn spring-boot:run

The client should then call the service every 2nd second and log its response.

Notice the response includes the name of the host where WildFly-Swarm is running.
This play a role later when we run this microservice in Docker and Kubernetes.

You can also call the hello service from a web browser using

    http://localhost:8080

#### Notice

The client with Spring-Boot does not include a HTTP server. If we were
to run with an embedded HTTP server then we would have a port clash on port 8080
as both Spring-Boot and WildFly-Swarm are using that port. If so you would need
to reconfigure one of them to use another port number.
    
You will not have such problem with Docker or Kubernetes. But they bring their
own set of networking problems to the table.
