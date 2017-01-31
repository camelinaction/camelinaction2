chapter7 - wildfly-swarm
------------------------

Running Camel with WildFly Swarm using CDI

### 7.2.3 WildFly Swarm with Camel as microservice 

You need to build this example first

    mvn install
    
And then you can run the example using
    
    mvn wildfly-swarm:run
    
And from a web browser open the following url:

    http://localhost:8080/hello

You can also run the example as a fat-jar using: 

    java -jar target/hello-swarm.jar
    
    
