Chapter 12 - propagation
------------------------

This directory holds an example how to use transaction propagation policies such as REQUIRES_NEW

### 12.3.5 - Using different transaction propagations

The example can be run with:

    mvn test -Dtest=PropagationTest
    mvn test -Dtest=PropagationRollbackLastTest
    mvn test -Dtest=SpringPropagationTest
    mvn test -Dtest=SpringPropagationRollbackLastTest

