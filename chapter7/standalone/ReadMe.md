chapter7 - standalone
---------------------

Running Camel as a standalone microservice.

### 7.2.1 Standalone Camel as microservice 

You can try this example by running this goal:

    mvn compile exec:java -Pmanual

And from a web browser open the following url:

    http://localhost:8080/hello
    
You can run Camel standalone using the `Main` class which is easier:
    
    mvn compile exec:java -Pmain
    
