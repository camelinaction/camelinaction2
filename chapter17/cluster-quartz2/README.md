Chapter 17 - cluster-quartz2
----------------------------

This directory holds an example how to use Quartz Scheduler to trigger routes to be
running within certain time windows in a master/slave mode.

### 17.6.1 Clustered scheduling using Quartz

#### Preparing database

This example requires a shared database (we are using Postgres) which you can run using Docker

    docker run -p 5432:5432 -e POSTGRES_USER=quartz -e POSTGRES_PASSWORD=quartz -d postgres
    
Then you need to initialize the database by running a script which you can run from Java with:

    mvn compile exec:java -P init

The SQL script to initialize the Postgres database is located in the `tables_postgres.sql` file.
You can find SQL scripts for other databases that Quartz supports if you download Quartz from
its website (http://www.quartz-scheduler.org/) and unzip the download and look in the 
`doc/dbTables` directory.

#### Running Camel

When the database is up and running its ready to accept connections on port 5432 and because
we have networked Docker then we can access on `localhost`. 

The example requires to run at two JVMs concurrently by starting each Maven goal from each terminal:

    mvn compile exec:java -Pfoo
    mvn compile exec:java -Pbar

You can then see that every minute the cron job is triggered and because its clustered, then its
 only one of the two Camel instances that runs the job. Quartz will run the job on the first node that
 grabs the lock from the database happens by random.
 
You can stop either instance of the running Camel and the running node will ensure to run the job.

For example here Foo runs the first two times, and the Bar runs the third time. Followed by Foo and Bar. 
Only one node runs the job at every minute.

```
2017-02-26 18:27:00,036 [amel-1_Worker-2] INFO  route1                         - Foo running at Sun Feb 26 18:27:00 CET 2017
2017-02-26 18:28:00,039 [amel-1_Worker-3] INFO  route1                         - Foo running at Sun Feb 26 18:28:00 CET 2017
2017-02-26 18:29:00,053 [amel-1_Worker-1] INFO  route1                         - Bar running at Sun Feb 26 18:29:00 CET 2017
2017-02-26 18:30:00,039 [amel-1_Worker-1] INFO  route1                         - Foo running at Sun Feb 26 18:30:00 CET 2017
2017-02-26 18:31:00,048 [amel-1_Worker-2] INFO  route1                         - Bar running at Sun Feb 26 18:31:00 CET 2017
```

If you stop one of the nodes then the existing running node will run the job. And when the other node comes back online
then they are both able to run the jobs again.

### Looking in the database

You can use a database query tool (IDEA has built-in database browser) which you can use to connect
to the database on

     jdbc:postgresql://localhost:5432/quartz
     
  with username: `quartz` and password: `quartz`.

Then you can browse the quartz tables and see what date it contains which holds the state of the
clustered quartz scheduler.
