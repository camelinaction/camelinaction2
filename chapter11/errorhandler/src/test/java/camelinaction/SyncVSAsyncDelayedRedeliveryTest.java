package camelinaction;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Shows the difference between using sync and async delayed redelivery
 */
public class SyncVSAsyncDelayedRedeliveryTest extends CamelTestSupport {

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry jndi = super.createRegistry();
        jndi.bind("orderService", new OrderService());
        return jndi;
    }

    @Override
    public boolean isUseRouteBuilder() {
        // dont use default route builder as we inline those in our test methods
        return false;
    }

    @Test
    public void testSyncOrderFailThenOK() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                errorHandler(deadLetterChannel("mock:dead")
                    .maximumRedeliveries(2)
                    .redeliveryDelay(1000)
                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                    .logStackTrace(false));

                from("seda:queue.inbox")
                    .log("Received input ${body}")
                    .beanRef("orderService", "validate")
                    .beanRef("orderService", "enrich")
                    .log("Received order ${body}")
                    .to("mock:queue.order");
            }
        });
        context.start();

        // notice that the order is preserved when we use sync redelivery

        // only the 2nd book will pass
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");
        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testAsyncOrderFailThenOK() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                errorHandler(deadLetterChannel("mock:dead")
                    // enable async delayed redelivery
                    .asyncDelayedRedelivery()
                    .maximumRedeliveries(2)
                    .redeliveryDelay(1000)
                    .retryAttemptedLogLevel(LoggingLevel.WARN)
                    .logStackTrace(false));

                from("seda:queue.inbox")
                    .log("Received input ${body}")
                    .beanRef("orderService", "validate")
                    .beanRef("orderService", "enrich")
                    .log("Received order ${body}")
                    .to("mock:queue.order");
            }
        });
        context.start();

        // notice that the order is NOT preserved when we use async redelivery
        // when the redelivery is scheduled for the 1st book, the consumer
        // will then be free to pickup the 2nd book and route it, while the
        // 1st book is still being scheduled to be redelivered in the future

        // only the 2nd book will pass
        MockEndpoint mock = getMockEndpoint("mock:queue.order");
        mock.expectedBodiesReceived("amount=1,name=Camel in Action,id=123,status=OK");

        // expect the 1st book to be moved into the dead letter queue
        MockEndpoint dead = getMockEndpoint("mock:dead");
        dead.expectedMessageCount(1);
        dead.message(0).body().contains("ActiveMQ");

        template.sendBody("seda:queue.inbox","amount=1,name=ActiveMQ in Action");
        template.sendBody("seda:queue.inbox","amount=1,name=Camel in Action");

        assertMockEndpointsSatisfied();
    }

}
