Chapter 12 - xa-database
------------------------

This directory holds an example how to use transaction that starts from a database and
sends to ActiveMQ using a global XA transaction.

### 12.3.3 Transaction starting from a database resource

The example can be run with:

    mvn test -Dtest=SpringXACommitTest
    mvn test -Dtest=SpringXARollbackBeforeActiveMQTest
    mvn test -Dtest=SpringXARollbackAfterActiveMQTest

