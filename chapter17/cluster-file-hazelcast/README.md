Chapter 17 - cluster-file-hazelcast
-----------------------------------

This directory holds an example how to use Hazelcast as clustered route policy in active/passive mode.

### 17.2.1 Clustered active/passive mode using Hazelcast

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

And then copy a bunch of files into the `target/inbox` directory which should let only the master
JVM consume the files. You can then kill/stop the master JVM and the slave JVM should become the
new master and consume the files.

