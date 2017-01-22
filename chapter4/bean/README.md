Chapter 4 - bean
----------------

This directory holds examples how to call Java Beans from Camel

### 4.1.2 - A Camel route using a Processor to invoke HelloBean

This example can be run using:

    mvn test -Dtest=InvokeWithProcessorTest
    mvn test -Dtest=InvokeWithProcessorSpringTest

### 4.1.3 - Using beans the easy way

This example can be run using:

    mvn test -Dtest=InvokeWithBeanTest
    mvn test -Dtest=InvokeWithBeanSpringTest

### 4.3.1 - Using JndiRegistry to unit test a Camel route

This example can be run using:

    mvn test -Dtest=JndiRegistryTest

### 4.3.2 - Using SimpleRegistry to unit test a Camel route

This example can be run using:

    mvn test -Dtest=SimpleRegistryTest

### 4.3.5 - A Camel route using CDI to invoke HelloBean

This example can be run using:

    mvn clean install exec:java

### 4.5.4 - Binding using Camel language annotations

This example can be run using:

    mvn test -Dtest=XmlOrderTest

### 4.5.4 - Using @JSonPath Binding Annotation

This example can be run using:

    mvn test -Dtest=JsonOrderTest

### 4.6.1 - Using compound predicates in Java

This example can be run using:

    mvn test -Dtest=CompoundPredicateTest

### 4.6.2 - Using beans as expressions in routes

This example can be run using:

    mvn test -Dtest=JsonExpressionTest
    mvn test -Dtest=SpringJsonExpressionTest

