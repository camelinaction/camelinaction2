Chapter 11 - xa
---------------

This directory holds examples how to use global transactions (XA aka 2-phase-commit)

### 12.3.2 - Using global transactions

The example can be run with:

    mvn test -Dtest=AtomikosXACommitTest

And rollback examples can be run with:

    mvn test -Dtest=AtomikosXARollbackBeforeDbTest
    mvn test -Dtest=AtomikosXARollbackAfterDbTest

