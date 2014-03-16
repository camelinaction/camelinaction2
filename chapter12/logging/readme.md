Chapter 12 -  Using custom logging
========================================

There are 3 examples demonstrating how to write to the logs using Camel.

The first example is an audit log example which uses an audit service bean that does the actual logging:

    mvn test -Dtest=AuditTest

The second example demonstrates using the Camel log component for logging:

    mvn test -Dtest=LogComponentTest

And the last example is similar to the Came log component, but uses the Log EIP instead:

    mvn test -Dtest=LogEIPTest
    mvn test -Dtest=LogEIPSpringTest

