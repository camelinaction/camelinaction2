package camelinaction;

import junit.framework.TestCase;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.junit.Test;

/**
 * Using {@link org.apache.camel.impl.JndiRegistry} as the Camel {@link org.apache.camel.spi.Registry}
 * to register beans and let Camel lookup them to be used in routes.
 */
public class JndiRegistryTest extends TestCase {

    private CamelContext context;
    private ProducerTemplate template;

    @Override
    protected void setUp() throws Exception {
        // TODO: workaround until Camel 2.16.2
        System.setProperty("java.naming.factory.initial", "org.apache.camel.util.jndi.CamelInitialContextFactory");

        // create the registry to be the JndiRegistry
        JndiRegistry registry = new JndiRegistry();
        // register our HelloBean under the name helloBean
        registry.bind("helloBean", new HelloBean());

        // tell Camel to use our JndiRegistry
        context = new DefaultCamelContext(registry);

        // create a producer template to use for testing
        template = context.createProducerTemplate();

        // add the route using an inlined RouteBuilder
        context.addRoutes(new RouteBuilder() {
            public void configure() throws Exception {
                from("direct:hello").bean("helloBean");
            }
        });
        // star Camel
        context.start();
    }

    @Override
    protected void tearDown() throws Exception {
        // cleanup resources after test
        template.stop();
        context.stop();
    }

    @Test
    public void testHello() throws Exception {
        // test by sending in World and expect the reply to be Hello World
        Object reply = template.requestBody("direct:hello", "World");
        assertEquals("Hello World", reply);
    }

}
