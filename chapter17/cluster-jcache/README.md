Chapter 17 - cluster-cache-infinispan
-------------------------------------

This directory holds an example how to use Infinispan as clustered distributed cache

### 17.1.4 Clustering Cache with Camel

#### Running Infinispan

You need to run an Infinispan Server which you can download from (chose Server)

    http://infinispan.org/download/
    
And then unzip Infinispan
     
    unzip infinispan-server-8.2.6.Final-bin.zip

Then we need to create an user

    cd infinispan-server-8.2.6.Final-bin
    ./bin/add-user.sh          (Windows users run the .bar file instead of .sh)               
    
.. create the user as administrator such as with `admin` as username and `admin` as password    

Then start Infinispan in domain mode 

    bin/domain.sh      (Windows users run the .bar file instead of .sh)
   
And then login to the web console at
   
    http://127.0.0.1:9990/console

In the web console you need to add a new cache with the name `myCache`. As a type you can choose `replicated`.

#### Running the Camel servers

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

From another shell you can call either the foo or the bar JVMs from a web browser with

To call the FOO server:

    http://localhost:8888   

To call the BAR server:

    http://localhost:8889

You should then be able to receive a response with a increasing counter that is distributed. So if you
mix the calls between the two JVMs then the counter is correct increased by one each time.

For example if we call the FOO route 4 times and then BAR route 3 times, and FOO route 2 times again, 
we can see from the logs of the two JVMs they output representative:

```
FOO: counter is now 1
FOO: counter is now 2
FOO: counter is now 3
FOO: counter is now 4
FOO: counter is now 8
FOO: counter is now 9

BAR: counter is now 5
BAR: counter is now 6
BAR: counter is now 7
```

You can try to restart either FOO or BAR which should ensure the counter survives in the cache and is updated correctly.

From the Infinispan web console you should be able to see cache statistics as shown in the screenshot below:

![Infinispan Web Console](infinispan-console.png "Cache Statistics")
