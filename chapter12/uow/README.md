Chapter 12 - uow
----------------

This directory holds examples how to use Camels `UnitOfWork` concept.

### 12.4.2 Using synchronization callbacks

The example can be run with:

    mvn test -Dtest=FileRollbackTest

### 12.4.3 - Using onCompletion

The example can be run with:

    mvn test -Dtest=FileRollbackOnCompletionTest
    mvn test -Dtest=SpringFileRollbackOnCompletionTest

### 12.4.3 - Using BeforeConsumer mode

This is a Rider Auto Parts use case with a Camel application that mobile application accesses to retrieve order information.
This example demonstrates the BeforeConsumer mode of `UnitOfWork`.

The example can be run with:

    mvn compile exec:java
    mvn compile exec:java -Pspring

You can call the service from a shell using curl

    curl -i http://localhost:8080/service/order/123

Notice we use the `-i` parameter to show the returned HTTP headers which should include the generated token.
