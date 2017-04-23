Chapter 17 - cluster-servicecall
--------------------------------

This directory holds an example how to use ServiceCall EIP to call services in a clustered or distributed system. 

### 17.7.2 Service Call using static service registry

The example uses a static list for service discovery.

#### Running the servers and the client

Then you can run the the two server JVMs concurrently by starting each Maven goal from each terminal:

    cd foo-server
    mvn spring-boot:run
    
    cd bar-server
    mvn spring-boot:run

Then you can start the client JVM

    cd client-static
    mvn camel:run -P embedded
    
  or if using XML DSL
    
    cd client-static-xml
    mvn camel:run -P embedded
    
The client then calls the foo and bar server using round robin mode.


### 17.7.4 Configuring Service Call EIP

The client can be configured to use global Service Call configuration.

Then you can start the client JVM

    cd client-static
    mvn camel:run -P global
    
  or if using XML DSL
    
    cd client-static-xml
    mvn camel:run -P global
    
The client then calls the foo and bar server using round robin mode.



### 17.7.6 Service Call using Spring Boot Cloud and Consul

The example uses Consul for service discovery, and runs using Spring Boot Cloud as the client.

#### Running consul

At first you must run a consul server which you can do using docker by running

     CURRENT=`pwd`
     docker run -it --rm -p 8500:8500 -v $CURRENT/consul:/consul/config consul

Notice to make the Docker volume able to mount the files from the relative `consul` folder
into the running container, you need to use the absolute path, which we can use `pwd` command
on osx and linux systems. If you use windows, you can specify the absolute path instead.

For example if the source code is located in

    c:\mystuff\camelinaction2
    
You can then run Docker with
    
     docker run -it --rm -p 8500:8500 -v c:\mystuff\camelinaction2\chapter17\cluster-serviecall\consul:/consul/config consul

When Consul startup you should see that it logs `hello-foo` and `hello-bar` in its log output

    2017/04/19 08:50:44 [INFO] agent: Synced service 'consul'
    2017/04/19 08:50:44 [INFO] agent: Synced service 'hello-foo'
    2017/04/19 08:50:44 [INFO] agent: Synced service 'hello-bar'

You can then open a web browser and view the consul web console at:

    http://localhost:8500/ui/

#### Running the servers and the client

Then you can run the the two server JVMs concurrently by starting each Maven goal from each terminal:

    cd foo-server
    mvn spring-boot:run
    
    cd bar-server
    mvn spring-boot:run

Then you can start the client JVM

    cd client-consul
    mvn spring-boot:run

The client then calls the foo and bar server using round robin mode.

