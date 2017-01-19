Chapter 11 - bridge
-------------------

This example demonstrates how to bridge the error handler between the consumer and Camel's routing engine.

For this example to work you would need a PostgresSQL database up and running.

The authors of the book used a docker image with a default postgres database setup.

The docker image is available from docker hub at: https://registry.hub.docker.com/_/postgres/

At the time of writing we used the latest version which is 9.4.1

To run the postgres SQL database docker image

    docker run --name my-postgres -p 5432:5432 -e POSTGRES_PASSWORD=secret -d postgres

This starts the database and exposes its service on port `5432`.
With the username `postgres` and password `secret`.

If you use boot2docker on osx, then the IP of the postgres database
is the boot2docker ip address which typically is `192.168.59.103`

You can see the status of the docker containers with

    docker ps

These settings has been defined in the JPA persistence file which is located in
`src/main/resources/META-INF/persistence.xml`. If your environment is different,
then you need to adjust this file accordingly.

The example is started from Maven using

    mvn clean install exec:java

The example prints instructions on the console what to do. At a point in the during the example you need
to stop the docker container that runs the postgres database, as each container is assigned an unique container id
you need to stop using that id (or use the container name),

For example

```
davsclaus:~/$ docker ps
CONTAINER ID        IMAGE               COMMAND                CREATED             STATUS              PORTS                    NAMES
b7d28c875855        postgres:latest     "/docker-entrypoint.   39 minutes ago      Up 12 minutes       0.0.0.0:5432->5432/tcp   my-postgres
davsclaus:~/$ docker stop b7d28c875855
b7d28c875855
```

And you can then start the container again using its id

```
davsclaus:~/$ docker start b7d28c875855
b7d28c875855
```

When running this example and following the instructions you should see something similar to the sample output below

    [INFO] --- exec-maven-plugin:1.1.1:java (default-cli) @ chapter11-bridge ---
    2016-05-11 20:08:19,502 [rderMain.main()] INFO  SpringCamelContext             - Apache Camel 2.16.0 (CamelContext: camel) is starting
    2016-05-11 20:08:19,732 [rderMain.main()] INFO  JpaComponent                   - Using EntityManagerFactory configured: org.springframework.orm.jpa.LocalEntityManagerFactoryBean@4befe1a1
    2016-05-11 20:08:19,734 [rderMain.main()] WARN  JpaComponent                   - No TransactionManager has been configured on this JpaComponent. Each JpaEndpoint will auto create their own JpaTransactionManager.
    2016-05-11 20:08:19,817 [rderMain.main()] INFO  SpringCamelContext             - AllowUseOriginalMessage is enabled. If access to the original message is not needed, then its recommended to turn this option off as it may improve performance.
    2016-05-11 20:08:19,817 [rderMain.main()] INFO  SpringCamelContext             - StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    2016-05-11 20:08:20,366 [rderMain.main()] INFO  SpringCamelContext             - Route: books started and consuming from: Endpoint[jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&consumer.bridgeErrorHandler=true&delay=1000]
    2016-05-11 20:08:20,367 [rderMain.main()] INFO  SpringCamelContext             - Total 1 routes, of which 1 is started.
    2016-05-11 20:08:20,368 [rderMain.main()] INFO  SpringCamelContext             - Apache Camel 2.16.0 (CamelContext: camel) started in 0.867 seconds
    2016-05-11 20:08:20,369 [rderMain.main()] INFO  MainSupport                    - Apache Camel 2.16.0 starting
    2016-05-11 20:08:20,371 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2016-05-11 20:08:20,371 [rderMain.main()] INFO  BookOrderExample               - Make sure to have Postgres database up and running, as configured in the src/test/resources/META-INF/persistence.xml file
    2016-05-11 20:08:20,371 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2016-05-11 20:08:21,529 [ction.BookOrder] INFO  books                          - Incoming book order 401 - 1 of Camel in Action 2nd ed
    2016-05-11 20:08:25,621 [rderMain.main()] INFO  BookOrderExample               - ... sleeping for 5 seconds and then stopping the route
    2016-05-11 20:08:25,622 [rderMain.main()] INFO  DefaultShutdownStrategy        - Starting to graceful shutdown 1 routes (timeout 300 seconds)
    2016-05-11 20:08:25,626 [ - ShutdownTask] INFO  DefaultShutdownStrategy        - Waiting as there are still 1 inflight and pending exchanges to complete, timeout in 300 seconds.
    2016-05-11 20:08:26,629 [ - ShutdownTask] INFO  DefaultShutdownStrategy        - Route: books shutdown complete, was consuming from: Endpoint[jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&consumer.bridgeErrorHandler=true&delay=1000]
    2016-05-11 20:08:26,629 [rderMain.main()] INFO  DefaultShutdownStrategy        - Graceful shutdown of 1 routes completed in 1 seconds
    2016-05-11 20:08:26,630 [rderMain.main()] INFO  SpringCamelContext             - Route: books is stopped, was consuming from: Endpoint[jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&consumer.bridgeErrorHandler=true&delay=1000]
    2016-05-11 20:08:26,700 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2016-05-11 20:08:26,700 [rderMain.main()] INFO  BookOrderExample               - Now we want to provoke a connection error, so stop the postgres database - and then press ENTER to continue!
    2016-05-11 20:08:26,700 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2016-05-11 20:08:38,415 [rderMain.main()] INFO  SpringCamelContext             - Route: books started and consuming from: Endpoint[jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&consumer.bridgeErrorHandler=true&delay=1000]
    2016-05-11 20:08:38,415 [rderMain.main()] INFO  BookOrderExample               - ... starting route which should indicate some errors, which the bridge error handler should catch and handle
    2016-05-11 20:08:38,415 [rderMain.main()] INFO  BookOrderExample               - Notice that the consumer will backoff and not poll so fast, instead of every second, it now runs x10 sec.
    2016-05-11 20:08:38,415 [rderMain.main()] INFO  BookOrderExample               - Press CTRL+C to exit this application!
    2016-05-11 20:08:39,425 [ction.BookOrder] WARN  books                          - Some exception happened but we do not care Connection to 192.168.59.103:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
    2016-05-11 20:08:50,441 [ction.BookOrder] WARN  books                          - Some exception happened but we do not care Connection to 192.168.59.103:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
    2016-05-11 20:09:01,456 [ction.BookOrder] WARN  books                          - Some exception happened but we do not care Connection to 192.168.59.103:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
    2016-05-11 20:09:12,536 [ction.BookOrder] INFO  books                          - Incoming book order 402 - 3 of ActiveMQ in Action
    ^C2016-05-11 20:09:19,336 [Thread-1       ] INFO  MainSupport$HangupInterceptor  - Received hang up - stopping the main instance.
    2016-05-11 20:09:19,336 [Thread-1       ] INFO  MainSupport                    - Apache Camel 2.16.0 stopping
    2016-05-11 20:09:19,337 [Thread-1       ] INFO  SpringCamelContext             - Apache Camel 2.16.0 (CamelContext: camel) is shutting down
    2016-05-11 20:09:19,338 [Thread-1       ] INFO  DefaultShutdownStrategy        - Starting to graceful shutdown 1 routes (timeout 300 seconds)
    2016-05-11 20:09:19,338 [ - ShutdownTask] INFO  DefaultShutdownStrategy        - Route: books shutdown complete, was consuming from: Endpoint[jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&consumer.bridgeErrorHandler=true&delay=1000]
    2016-05-11 20:09:19,338 [Thread-1       ] INFO  DefaultShutdownStrategy        - Graceful shutdown of 1 routes completed in 0 seconds
    2016-05-11 20:09:19,345 [Thread-1       ] INFO  SpringCamelContext             - Apache Camel 2.16.0 (CamelContext: camel) uptime 59.846 seconds
    2016-05-11 20:09:19,345 [Thread-1       ] INFO  SpringCamelContext             - Apache Camel 2.16.0 (CamelContext: camel) is shutdown in 0.007 seconds
