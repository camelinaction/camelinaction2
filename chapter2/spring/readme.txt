Spring Routing Example
----------------------

These examples show you how to configure Camel from Spring. The first 
is a simple demonstration of pure Spring wiring of beans. Run it by 
executing the following on the command line:

mvn compile exec:java -Dexec.mainClass=camelinaction.GreetMeBean

The next example loads orders from a local directory and sends them
to a JMS queue. Execute the following to run this example:

mvn compile camel:run


