Chapter 17 - cluster-cache-hazelcast
------------------------------------

This directory holds an example how to use Hazelcast as clustered distributed cache

### 17.5.1 Clustered cache using Hazelcast

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

From another shell you can call either the foo or the bar JVMs from a web browser with

To call the Foo server:

    http://localhost:8080   

To call the Bar server:

    http://localhost:9090

You should then be able to receive a response with a increasing counter that is distributed. So if you
mix the calls between the two JVMs then the counter is correct increased by one each time.

For example if we call the Foo route 4 times and then Bar route 3 times, and Foo route 2 times again, 
we can see from the logs of the two JVMs they output representative:

```
Foo: counter is now 1
Foo: counter is now 2
Foo: counter is now 3
Foo: counter is now 4
Foo: counter is now 8
Foo: counter is now 9

Bar: counter is now 5
Bar: counter is now 6
Bar: counter is now 7
```

You can try to restart either Foo or Bar which should ensure the counter survives in the cache and is updated correctly.

#### Running with atomic counter

This example can also use the atomic-counter instead which you can run using

    mvn compile exec:java -Pfoo-atomic
    mvn compile exec:java -Pbar-atomic

See more details in `CounterRoute` and `AtomicCounterRouter.java` source code.