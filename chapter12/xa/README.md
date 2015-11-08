Chapter 12 - xa
---------------

This directory holds examples how to use global transactions (XA aka 2-phase-commit)

### 12.3.2 - Using global transactions

The example can be run with:

    mvn test -Dtest=XACommitTest
    mvn test -Dtest=SpringXACommitTest

The commit example is designed to not fail and therefore successfully consume from the message queue and insert data
into the database using a transaction that commits.

And rollback examples can be run with:

    mvn test -Dtest=XARollbackBeforeDbTest
    mvn test -Dtest=SpringXARollbackBeforeDbTest

    mvn test -Dtest=XARollbackAfterDbTest
    mvn test -Dtest=SpringXARollbackAfterDbTest

The rollback examples are designed to throw an exception either before or after inserting data into the database,
and therefore cause the transaction to rollback.

