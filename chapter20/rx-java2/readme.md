Chapter 20 - rx-java2
---------------------

This directory holds beginner examples with standalone RX-Java 2.x
and when using it together with Camel.

### 21.1.2 First steps with Reactive Streams

The example can be run with:

    mvn test -Dtest=FirstTest

### 21.1.3 Using Camel with Reactive Streams

The example can be run with:

    mvn test -Dtest=CamelFirstTest

### Using Camel route as Reactive Stream Publisher

The example can be run with:

    mvn test -Dtest=CamelNumbersTest
    
#### Using Camel route as Reactive Stream Subscriber

The example can be run with:

    mvn test -Dtest=CamelConsumeNumbersTest
    
#### Using regular Camel components in Reactive flow

The example can be run with:

    mvn test -Dtest=CamelFilesTest
    
### 21.1.4 Controlling back pressure from producer side
    
The example can be run with:

    mvn test -Dtest=CamelNoBackPressureTest
        
#### Adding back pressure

The example can be run with:

    mvn test -Dtest=CamelInflightBackPressureTest
            
#### Using alternative back pressure strategies
            
The example can be run with:

    mvn test -Dtest=CamelLatestBackPressureTest
                
### 21.1.5 Controlling back pressure from consumer side
                
The example can be run with:

    mvn test -Dtest=CamelConsumerBackPressureTest                
