Chapter 17 - cluster-cache-hazelcast
------------------------------------

This directory holds an example how to use Hazelcast as clustered distributed cache

### 17.1.4 Clustering Cache with Camel

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

From another shell you can call either the foo or the bar JVMs from a web browser with

To call the FOO server:

    http://localhost:8080   

To call the BAR server:

    http://localhost:9090

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