Chapter 5 - loadbalancer
----------------

This directory holds examples of the loadbalancer EIP.

### 5.6.1 - Load balance between two SEDA endpoints using round robin strategy

This example can be run using:

	mvn test -Dtest=LoadBalancerTest
	mvn test -Dtest=SpringLoadBalancerTest

### 5.6.2 - The random, sticky, circuit breaker, and topic strategies load balancer strategies

The examples can be run using:

	mvn test -Dtest=RandomLoadBalancerTest
	mvn test -Dtest=SpringRandomLoadBalancerTest
	mvn test -Dtest=StickyLoadBalancerTest
	mvn test -Dtest=SpringStickyLoadBalancerTest
	mvn test -Dtest=CircuitBreakerLoadBalancerTest
	mvn test -Dtest=SpringCircuitBreakerLoadBalancerTest
	mvn test -Dtest=TopicLoadBalancerTest
	mvn test -Dtest=SpringTopicLoadBalancerTest

### 5.6.3 - Fail over load balancer

The first example fails over to 2nd endpoint on an Exception:

	mvn test -Dtest=FailoverLoadBalancerTest
	mvn test -Dtest=SpringFailoverLoadBalancerTest

Next we use a round robin strategy when failing over:

	mvn test -Dtest=FailoverRoundRobinLoadBalancerTest
	mvn test -Dtest=SpringFailoverRoundRobinLoadBalancerTest

Final example is using the inheritErrorHandler configuration option:

	mvn test -Dtest=FailoverInheritErrorHandlerLoadBalancerTest
	mvn test -Dtest=SpringFailoverInheritErrorHandlerLoadBalancerTest

### 5.6.4 - Using a custom load balancer

This example can be run using:

	mvn test -Dtest=CustomLoadBalancerTest
	mvn test -Dtest=SpringCustomLoadBalancerTest
