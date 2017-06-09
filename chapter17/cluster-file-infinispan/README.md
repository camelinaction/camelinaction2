Chapter 17 - cluster-file-infinispan
-----------------------------------

This directory holds an example how to use Infinispan as clustered route policy in active/passive mode.

#### Running Infinispan

You need to run an Infinispan Server which you can download from (chose Server)

    http://infinispan.org/download/
    
And then unzip Infinispan
     
    unzip infinispan-server-9.0.1.Final-bin.zip

Then we need to create an user

    cd infinispan-server-9.0.1.Final-bin
    ./bin/add-user.sh          (Windows users run the .bar file instead of .sh)               
    
.. create the user as administrator such as with `admin` as username and `admin` as password    

Then start Infinispan in domain mode 

    bin/domain.sh      (Windows users run the .bar file instead of .sh)
   
And then login to the web console at
   
    http://127.0.0.1:9990/console

In the web console you need to add a new cache with the name `myLock`. As a type you can choose `replicated`.

### 17.2.1 Clustered active/passive mode using Infinispan

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

And then copy a bunch of files into the `target/inbox` directory which should let only the master
JVM consume the files. You can then kill/stop the master JVM and the slave JVM should become the
new master and consume the files.

