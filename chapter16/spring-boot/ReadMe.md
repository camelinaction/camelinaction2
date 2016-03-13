# chapter 16 - spring-boot
--------------------------

This example shows how to work with the simple Camel application based on the Spring Boot.

The example generates messages using timer trigger, writes them to the standard output.

The example allows remote management using SSH where you cab use the Camel Commands to manage
your Camel application.

### 16.4.4 Using Camel Commands with Spring-Boot

You will need to compile this example first:

	mvn install

To run the example type

	mvn spring-boot:run

You can also execute the fat WAR directly:

	java -jar target/spring-boot.war

You can SSH into the JVM using

    ssh -p 2000 user@localhost

And then use the generated password that spring-boot logged when it was staring.
