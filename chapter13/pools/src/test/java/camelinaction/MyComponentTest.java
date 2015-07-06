package camelinaction;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

/**
 * Test using MyComponent
 */
public class MyComponentTest extends CamelTestSupport {

    @Test
    public void testMyComponent() throws Exception {
        // add my component to Camel
        context.addComponent("my", new MyComponent());

        // start my component
        context.getComponent("my", MyComponent.class).start();

        System.out.println("Waiting for 10 seconds before we shutdown");
        Thread.sleep(10000);
    }
}
