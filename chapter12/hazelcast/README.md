Chapter 12 - hazelcast
----------------------

This directory holds an example how to use Hazelcast as clustered idempotent repository.

### 12.5.3 - Clustered idempotent repository

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar



