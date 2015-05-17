Chapter 1 - File Copy Example
=======================

This example is copying files from data/inbox to data/outbox directory.


Running
-----------

To run the example:

    mvn compile exec:java -Dexec.mainClass=camelinaction.FileCopierWithCamel
    
When the example is done, then you can see that the file has been copied from `data/inbox/message1.xml` to `data/outbox/message1.xml`.

