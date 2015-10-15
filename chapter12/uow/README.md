Chapter 12 - uow
----------------

This directory holds examples how to use Camels `UnitOfWork` concept.

### 12.4.3 - Using onCompletion

The example can be run with:

    mvn test -Dtest=FileRollbackTest
    mvn test -Dtest=SpringFileRollbackTest

### 12.4.3 - Using BeforeConsumer mode

This is a Rider Auto Parts use case with a Camel application that mobile application accesses to retrieve order information.
This example demonstrates the BeforeConsumer mode of `UnitOfWork`.

The example can be run with:

    mvn compile exec:java
    mvn compile exec:java -Pspring

