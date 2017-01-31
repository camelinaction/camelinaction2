chapter7 - wildfly-swarm-spring
-------------------------------

Running Camel with Spring XML on WildFly Swarm

### 7.2.3 WildFly Swarm with Camel as microservice 

You need to build this example first

    mvn install
    
And then you can run the example using
    
    mvn wildfly-swarm:run
    
And from a web browser open the following url:

    http://localhost:8080/hello

You can also run the example as a fat-jar using: 

    java -jar target/hello-spring-swarm.jar
    
This example contains the Spring XML file in the `src/main/resources/spring`
directory which WildFly Camel automatic detects and loads. The name of the
file must have suffix `-camel-context.xml`.
    
