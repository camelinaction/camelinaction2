Chapter 11 - bridge
-------------------

This example demonstrates how to bridge the error handler between the consumer and Camel's routing engine.

### JPA configuration

JPA is configured in the `src/main/resources/META-INF/persistence.xml` file.

### Docker

For this example to work you would need a PostgresSQL database up and running.

The authors of the book used a docker image with a default postgres database setup.

The docker image is available from docker hub at: https://registry.hub.docker.com/_/postgres/

At the time of writing we used the latest version which is 9.6.5

To run the postgres SQL database docker image

    docker run --name my-postgres -p 5432:5432 -e POSTGRES_PASSWORD=secret -d postgres

This starts the database and exposes its service on port `5432`.
With the username `postgres` and password `secret`.

You can see the status of the docker containers with

    docker ps

### Running Camel

The example is started from Maven using

    mvn clean install exec:java

The example prints instructions on the console what to do. At a point in the during the example you need
to stop the docker container that runs the postgres database, as each container is assigned an unique container id
you need to stop using that id (or use the container name),

For example

```
davsclaus:~/$ docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
9dab32d55b13        postgres            "docker-entrypoint..."   8 minutes ago       Up 8 minutes        0.0.0.0:5432->5432/tcp   my-postgres

davsclaus:~/$ docker stop 9dab32d55b13
9dab32d55b13
```

And you can then start the container again using its id

```
davsclaus:~/$ docker start 9dab32d55b13
9dab32d55b13
```

When running this example and following the instructions you should see something similar to the sample output below

    [INFO] --- exec-maven-plugin:1.1.1:java (default-cli) @ chapter11-bridge ---
    2017-10-21 09:43:30,983 [rderMain.main()] INFO  ClassPathXmlApplicationContext - Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@1bd969cd: startup date [Sat Oct 21 09:43:30 CEST 2017]; root of context hierarchy
    2017-10-21 09:43:31,041 [rderMain.main()] INFO  XmlBeanDefinitionReader        - Loading XML bean definitions from class path resource [camelinaction/camel-bridge.xml]
    2017-10-21 09:43:32,150 [rderMain.main()] INFO  LocalEntityManagerFactoryBean  - Building JPA EntityManagerFactory for persistence unit 'book'
    2017-10-21 09:43:32,292 [rderMain.main()] INFO  LocalEntityManagerFactoryBean  - Initialized JPA EntityManagerFactory for persistence unit 'book'
    2017-10-21 09:43:32,329 [rderMain.main()] INFO  SpringCamelContext             - Apache Camel 2.20.1 (CamelContext: camel) is starting
    2017-10-21 09:43:32,329 [rderMain.main()] INFO  ManagedManagementStrategy      - JMX is enabled
    2017-10-21 09:43:32,486 [rderMain.main()] INFO  DefaultTypeConverter           - Type converters loaded (core: 192, classpath: 1)
    2017-10-21 09:43:32,585 [rderMain.main()] INFO  SpringCamelContext             - StreamCaching is not in use. If using streams then its recommended to enable stream caching. See more details at http://camel.apache.org/stream-caching.html
    2017-10-21 09:43:32,588 [rderMain.main()] INFO  JpaComponent                   - Using EntityManagerFactory configured: org.springframework.orm.jpa.LocalEntityManagerFactoryBean@2f974e1e
    2017-10-21 09:43:32,590 [rderMain.main()] WARN  JpaComponent                   - No TransactionManager has been configured on this JpaComponent. Each JpaEndpoint will auto create their own JpaTransactionManager.
    2017-10-21 09:43:33,219 [rderMain.main()] INFO  SpringCamelContext             - Route: books started and consuming from: jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&bridgeErrorHandler=true&delay=1000
    2017-10-21 09:43:33,219 [rderMain.main()] INFO  SpringCamelContext             - Total 1 routes, of which 1 are started
    2017-10-21 09:43:33,220 [rderMain.main()] INFO  SpringCamelContext             - Apache Camel 2.20.1 (CamelContext: camel) started in 0.892 seconds
    2017-10-21 09:43:33,224 [rderMain.main()] INFO  DefaultLifecycleProcessor      - Starting beans in phase 2147483646
    2017-10-21 09:43:33,225 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2017-10-21 09:43:33,225 [rderMain.main()] INFO  BookOrderExample               - Make sure to have Postgres database up and running, as configured in the src/test/resources/META-INF/persistence.xml file
    2017-10-21 09:43:33,225 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2017-10-21 09:43:34,363 [ction.BookOrder] INFO  books                          - Order 1 - Camel in Action 2nd ed
    2017-10-21 09:43:38,371 [rderMain.main()] INFO  BookOrderExample               - ... sleeping for 5 seconds and then stopping the route
    2017-10-21 09:43:38,372 [rderMain.main()] INFO  DefaultShutdownStrategy        - Starting to graceful shutdown 1 routes (timeout 300 seconds)
    2017-10-21 09:43:38,375 [ - ShutdownTask] INFO  DefaultShutdownStrategy        - Route: books shutdown complete, was consuming from: jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&bridgeErrorHandler=true&delay=1000
    2017-10-21 09:43:38,376 [rderMain.main()] INFO  DefaultShutdownStrategy        - Graceful shutdown of 1 routes completed in 0 seconds
    2017-10-21 09:43:38,376 [rderMain.main()] INFO  SpringCamelContext             - Route: books is stopped, was consuming from: jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&bridgeErrorHandler=true&delay=1000
    2017-10-21 09:43:38,400 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    2017-10-21 09:43:38,400 [rderMain.main()] INFO  BookOrderExample               - Now we want to provoke a connection error, so stop the postgres database - and then press ENTER to continue!
    2017-10-21 09:43:38,400 [rderMain.main()] INFO  BookOrderExample               - -------------------------------------------------------------------------------------------------------------------------
    
    2017-10-21 09:44:42,306 [rderMain.main()] INFO  SpringCamelContext             - Route: books started and consuming from: jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&bridgeErrorHandler=true&delay=1000
    2017-10-21 09:44:42,307 [rderMain.main()] INFO  BookOrderExample               - ... starting route which should indicate some errors, which the bridge error handler should catch and handle
    2017-10-21 09:44:42,307 [rderMain.main()] INFO  BookOrderExample               - Notice that the consumer will backoff and not poll so fast, instead of every second, it now runs x10 sec.
    2017-10-21 09:44:42,307 [rderMain.main()] INFO  BookOrderExample               - Press CTRL+C to exit this application!
    2017-10-21 09:44:43,336 [ction.BookOrder] WARN  books                          - We do not care <openjpa-2.4.1-r422266:1730418 fatal general error> org.apache.openjpa.persistence.PersistenceException: Connection to 0.0.0.0:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
    2017-10-21 09:44:54,370 [ction.BookOrder] WARN  books                          - We do not care <openjpa-2.4.1-r422266:1730418 fatal general error> org.apache.openjpa.persistence.PersistenceException: Connection to 0.0.0.0:5432 refused. Check that the hostname and port are correct and that the postmaster is accepting TCP/IP connections.
    2017-10-21 09:45:05,424 [ction.BookOrder] INFO  books                          - Order 2 - ActiveMQ in Action
    2017-10-21 09:45:53,481 [ngupInterceptor] INFO  MainSupport$HangupInterceptor  - Received hang up - stopping the main instance.
    2017-10-21 09:45:53,482 [ngupInterceptor] INFO  ClassPathXmlApplicationContext - Closing org.springframework.context.support.ClassPathXmlApplicationContext@1bd969cd: startup date [Sat Oct 21 09:43:30 CEST 2017]; root of context hierarchy
    2017-10-21 09:45:53,483 [ngupInterceptor] INFO  DefaultLifecycleProcessor      - Stopping beans in phase 2147483646
    2017-10-21 09:45:53,483 [ngupInterceptor] INFO  SpringCamelContext             - Apache Camel 2.20.1 (CamelContext: camel) is shutting down
    2017-10-21 09:45:53,483 [ngupInterceptor] INFO  DefaultShutdownStrategy        - Starting to graceful shutdown 1 routes (timeout 300 seconds)
    2017-10-21 09:45:53,483 [ - ShutdownTask] INFO  DefaultShutdownStrategy        - Route: books shutdown complete, was consuming from: jpa://camelinaction.BookOrder?backoffErrorThreshold=1&backoffMultiplier=10&bridgeErrorHandler=true&delay=1000
    2017-10-21 09:45:53,483 [ngupInterceptor] INFO  DefaultShutdownStrategy        - Graceful shutdown of 1 routes completed in 0 seconds
    2017-10-21 09:45:53,497 [ngupInterceptor] INFO  MainLifecycleStrategy          - CamelContext: camel has been shutdown, triggering shutdown of the JVM.
    2017-10-21 09:45:53,501 [ngupInterceptor] INFO  SpringCamelContext             - Apache Camel 2.20.1 (CamelContext: camel) uptime 2 minutes
    2017-10-21 09:45:53,501 [ngupInterceptor] INFO  SpringCamelContext             - Apache Camel 2.20.1 (CamelContext: camel) is shutdown in 0.018 seconds
    2017-10-21 09:45:53,502 [ngupInterceptor] INFO  LocalEntityManagerFactoryBean  - Closing JPA EntityManagerFactory for persistence unit 'book'
