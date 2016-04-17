package camelinaction;

import java.io.File;
import java.util.Dictionary;
import java.util.Map;
import java.util.Properties;

import org.apache.camel.Exchange;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.apache.camel.util.KeyValueHolder;
import org.junit.Test;

/**
 * An unit test that uses a shared OSGi service which is not available when running
 * outside the real OSGi container using camel-test-blueprint.
 * So we need to enlist the service manually from this unit test.
 */
public class BlueprintServiceTest extends CamelBlueprintTestSupport {

    public void setUp() throws Exception {
        // delete directories so we have a clean start
        deleteDirectory("target/inbox");
        deleteDirectory("target/outbox");
        super.setUp();
    }

    @Override
    protected void addServicesOnStartup(Map<String, KeyValueHolder<Object, Dictionary>> services) {
        // create our fake mock service which will send the message to "mock:audit"
        MockAuditService mock = new MockAuditService();
        services.put(AuditService.class.getName(), asService(mock, null));
    }

    @Override
    protected String getBlueprintDescriptor() {
        // refer to where in the classpath the blueprint XML file is located
        return "OSGI-INF/blueprint/audit.xml";
    }

    @Test
    public void testMoveFile() throws Exception {
        // the fake mock should send a message to this mock endpoint
        getMockEndpoint("mock:audit").expectedMessageCount(1);

        // create a new file in the inbox folder with the name hello.txt and containing Hello World as body
        template.sendBodyAndHeader("file://target/inbox", "Hello World", Exchange.FILE_NAME, "hello.txt");

        // wait a while to let the file be moved
        Thread.sleep(2000);

        // test the file was moved
        File target = new File("target/outbox/hello.txt");
        assertTrue("File should have been moved", target.exists());

        // test that its content is correct as well
        String content = context.getTypeConverter().convertTo(String.class, target);
        assertEquals("Hello World", content);

        assertMockEndpointsSatisfied();
    }

}
