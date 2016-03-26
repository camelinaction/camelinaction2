package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * Unit test to show how to use Camel properties component with the Java DSL.
 * We test the Hello World example of integration kits, which is moving a file.
 * <p/>
 * This unit test is reusing the unit test which was designated to be
 * tested in the production environment. Notice how we extend that class
 * and the only difference is that we set a different location for
 * the properties file on the PropertiesComponent
 */
public class CamelRiderJavaDSLTest extends CamelRiderJavaDSLProdTest {

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = super.createCamelContext();

        // setup the properties component to use the test file
        PropertiesComponent prop = context.getComponent("properties", PropertiesComponent.class);
        prop.setLocation("classpath:rider-test.properties");

        return context;
    }

}
