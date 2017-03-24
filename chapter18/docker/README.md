Chapter 18 - docker
-------------------

This holds the Docker version of the hello microservice.

### 18.1.2 Building and running Camel microservices using Docker

At first you need to run Docker

#### Running Docker

You can install and run docker by downloading the community version from

    https://www.docker.com/
    
And follow the installation guide. When docker is running you should be able to run
the following Docker commands from a command line:
    
    docker info
    docker images
    docker ps
    
#### Running WildFly-Swarm

At first you need to run the WildFly-Swarm service which hosts the hello service 
that the Spring-Boot client will call.

    cd hello-swarm
    mvn install docker:build
    
Which will build a docker image with the application. You can see the image from docker with
 
    $ docker images
    REPOSITORY                         TAG                           IMAGE ID            CREATED             SIZE
    camelinaction/helloswarm-docker    latest                        a0d46b208134        17 minutes ago      755 MB

You are now ready to run the WildFly-Swarm application using Docker.

You can run the application in interactive mode with `-it` which means it runs in foreground.
You also need to enable port mapping so we can access the service from your host operating system
so we do that with `-p8080:8080`

    docker run -it -p8080:8080 camelinaction/helloswarm-docker:latest 

You can test if the WildFly-Swarm application runs correctly by opening from a web browser:

    http://localhost:8080

#### Running Spring-Boot    

The Spring-Boot client needs to be built as docker image first 

    cd client-spring
    mvn install docker:build
    
Which will build a docker image with the application. You can see the image from docker with
 
    $ docker images
    REPOSITORY                         TAG                           IMAGE ID            CREATED             SIZE
    camelinaction/spring-docker        latest                        a0ba61b85c6f        8 minutes ago       657 MB

You are now ready to run the Spring-Boot application using Docker.

You can run the application in interactive mode with `-it` which means it runs in foreground.
You do not need to enable port mapping as the client is standalone and we dont need to call it from
your host operating system.

    docker run -it camelinaction/spring-docker:latest 

If the application runs successfully you should set the client logs the response from WildFly-Swarm

    route1                                   : Swarm says hello from 4bd8febb98de
    route1                                   : Swarm says hello from 4bd8febb98de
    route1                                   : Swarm says hello from 4bd8febb98de

#### IP number

When you run Docker applications using plain Docker then each Docker process is assigned
an IP address. So the WildFly-Swarm application is assigned an IP address which we need
to know so when the Spring-Boot client wants to call it, it must use that IP number.

Usually the first process is assigned IP `172.0.0.2` and then upwards.


#### Notice

The client with Spring-Boot does not include a HTTP server. If we were
to run with an embedded HTTP server then we would have a port clash on port 8080
as both Spring-Boot and WildFly-Swarm are using that port. If so you would need
to reconfigure one of them to use another port number.
    
You will not have such problem with Docker or Kubernetes. But they bring their
own set of networking problems to the table.  
