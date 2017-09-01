package camelinaction;

import org.apache.camel.ThreadPoolRejectedPolicy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolProfileBuilder;
import org.apache.camel.spi.ThreadPoolProfile;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * How to configure a custom thread pool profile in Java DSL
 */
public class CustomThreadPoolProfileTest extends CamelTestSupport {

    public ThreadPoolProfile createCustomProfile() {
        // create a custom thread pool profile with the name bigPool
        return new ThreadPoolProfileBuilder("bigPool")
            .maxPoolSize(200)
            .rejectedPolicy(ThreadPoolRejectedPolicy.DiscardOldest)
            .build();
    }

    @Test
    public void testCustomThreadPoolProfile() throws Exception {
        getMockEndpoint("mock:result").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello Camel");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // register a custom thread pool profile
                ThreadPoolProfile custom = createCustomProfile();
                context.getExecutorServiceManager().registerThreadPoolProfile(custom);

                from("direct:start")
                    // use the bigPool profile for creating the thread pool to be used
                    .threads().executorServiceRef("bigPool")
                    .to("log:foo")
                    .to("mock:result");
            }
        };
    }
}
