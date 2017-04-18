Chapter 17 - cluster-servicecall
--------------------------------

This directory holds an example how to use ServiceCall EIP to call services in a clustered or distributed system. 

The example uses Consul for service discovery.

### 17.7 Calling clustered services using Service Call EIP

At first you must run a consul server which you can do using docker by running

     docker run -it --rm -p 8500:8500 -v consul:/consul/config consul

You can then open a web browser and view the consul web console at:

    http://localhost:8500/ui/

Then you can run the the two server JVMs concurrently by starting each Maven goal from each terminal:

    cd foo-server
    mvn spring-boot:run
    
    cd bar-server
    mvn spring-boot:run

Then you can start the client JVM

    cd client
    mvn spring-boot:run

TODO: more bla bla