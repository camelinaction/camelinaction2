package camelinaction;

import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ThreadPoolBuilderTest extends CamelTestSupport {

    @Test
    public void testThreadPoolBuilder() throws Exception {
        getMockEndpoint("mock:result").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // create a thread pool builder
                ThreadPoolBuilder builder = new ThreadPoolBuilder(context);

                // use thread pool builder to create a custom thread pool
                ExecutorService myPool = builder.poolSize(5).maxPoolSize(25).maxQueueSize(200).build("MyPool");

                from("direct:start")
                    // use our custom pool in the threads DSL
                    .threads().executorService(myPool)
                        .to("log:cool")
                        .to("mock:result")
                    .end();
            }
        };
    }
}
