Chapter 16 - custom
-------------------

This example shows how to add custom JMX management attributes and operations to your custom developed Camel components.

### 16.5.4 - Management enable custom Camel components

This example can be run with:

    mvn compile exec:java

Then connect to the running application from jconsole, where you can find the custom component in the `org.apache.camel.components` tree.
From there you can turn on or off the `Verbose` attribute.

