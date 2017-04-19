Chapter 18 - kubernetes
-----------------------

This holds the Kubernetes version of the hello microservice.

### 18.2 Getting started with Camel on Kubernetes

At first you need to run Kubernetes.
See section 18.2.1 in the book.

#### Running WildFly-Swarm

At first you need to run the WildFly-Swarm service which hosts the hello service 
that the Spring-Boot client will call.

Before deploying it's a good idea to setup the command shell to work with the cluster

    eval $(minikube docker-env)

This makes docker work with the docker registry that runs inside the cluster.
Which means we can then safely build and deploy the application as a docker image

    cd hello-swarm
    mvn clean install fabric8:deploy

This will deploy the application in the local Kubernetes cluster.

#### Running Spring-Boot    

Before deploying it's a good idea to setup the command shell to work with the cluster

    eval $(minikube docker-env)

This makes docker work with the docker registry that runs inside the cluster.
Which means we can then safely build and deploy the application as a docker image

You can then run the Spring-Boot client

    cd client-spring
    mvn clean install fabric8:run

Notice we use `fabric8:run` which runs the application and automatic tail the log from the running
    pod. You should see the client calls the hello service and logs the response:
    
    route1    Swarm says hello from helloswarm-kubernetes-4221986457-3w1q9
    route1    Swarm says hello from helloswarm-kubernetes-4221986457-3w1q9
    route1    Swarm says hello from helloswarm-kubernetes-4221986457-3w1q9
    
You can then press `ctrl + c` to stop the application and it will automatically be undeployed.
    
An alternative is to deploy the applicatiom using `mvn clean install fabric8:deploy`, and then
    you can tail the logs with `mvn fabric8:log`.
    
You can also use the `kubectl` client to tail logs.    
    
