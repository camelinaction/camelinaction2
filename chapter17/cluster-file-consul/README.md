Chapter 17 - cluster-file-consul
--------------------------------

This directory holds an example how to use Consul as clustered route policy in active/passive mode.

### 17.2.2 Clustered active/passive mode using Consul

At first you must run a consul server which you can do using docker by running

     docker run -it --rm -p 8500:8500 consul

You can then open a web browser and view the consul web console at:

    http://localhost:8500/ui/

Then you can run the the two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

And then copy a bunch of files into the `target/inbox` directory which should let only the master
JVM consume the files. You can then kill/stop the master JVM and the slave JVM should become the
new master and consume the files.

