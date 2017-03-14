Chapter 17 - cluster-zookeeper-master
-------------------------------------

This directory holds an example how to use ZooKeeper as clustered master route in active/passive mode.

### 17.2.3 Clustered active/passive mode using ZooKeeper

At first you must run a ZooKeeper server which you can do using Docker by running

     docker run -it --rm -p 2181:2181 -d zookeeper

Then you can run the the two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

And then copy a bunch of files into the `target/inbox` directory which should let only the master
JVM consume the files. You can then kill/stop the master JVM and the slave JVM should become the
new master and consume the files.


