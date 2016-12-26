chapter7 - hystrix-camel
------------------------

Using Hystrix with Apache Camel in your Camel routes to
use circuit breaker that handles failures.

### 7.4.5 Using Camel with Hystrix

You can run the example using:
    
    mvn test -Dtest=CamelHystrixTest
    
And the example which includes a fallback:
    
    mvn test -Dtest=CamelHystrixWithFallbackTest
    
The example is also provided as a XML DSL alternative which you can try:
    
    mvn test -Dtest=SpringCamelHystrixTest
    
.. and also with a fallback:    
    
    mvn test -Dtest=SpringCamelHystrixWithFallbackTest
    

