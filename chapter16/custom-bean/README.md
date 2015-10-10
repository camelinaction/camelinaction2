Chapter 16 - custom-bean
------------------------

This example shows how to add custom JMX management attributes and operations to your custom developed Java beans which are used in Camel routes.

### 16.5.5 - Management enable custom Java beans

This example can be run with:

    mvn compile exec:java

Then connect to the running application from jconsole, where you can find the custom bean in the `org.apache.camel.processors` tree.
From there you can change the `Greeting` attribute.

