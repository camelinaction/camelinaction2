Chapter 18 - hellowswarm-standalone
-----------------------------------

This holds the standalone version of the hello microservice.

### 18.1.1 Building and running Camel microservices locally

You can run WildFly-Swarm hello service locally by running

    mvn wildfly-swarm:run
    
And then you can call the service using a browser at:
    
    http://localhost:8080/say
    
Which should return a response with the hostname where swarm is running.    
