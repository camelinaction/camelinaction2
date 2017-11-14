chapter19 - hawtio debug
------------------------

This shows how to debug a Camel route using the hawtio web console.

### 19.3.2 Debugging Camel routes using hawtio

Because we want to debug the route, we can start the example with hawtio embedded.
This can be done by running the following Maven goal:

    mvn compile hawtio:run

Then hawtio opens automatic in a web browser, where you from the Camel plugin select
the route to debug in the route tree on the left hand side. Then there is a `Debug` button
in the middle panel. Then you can double click an EIP icon to set a break point.

When a message hits the breakpoint the ball changes color to blue. You can then inspect
the message in the table in the bottom. You can then continue routing the message
by clicking either the run or single step button.


#### Connecting hawtio to existing running JVM

If you have any existing Java application running in a JVM, you can startup hawtio in standalone mode
and then from hawtio connect to the existing JVM. In the example instead of running the hawto:run plugin,
you run exec:java instead

    mvn exec:java

And then from another terminal you download hawtio-app and run it as a standalone Java application:

    java -jar hawtio-app-1.5.6.jar

(where 1.5.6 is the version of hawtio)
