package camelinaction;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.condition.AbstractCondition;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.jms.message.JmsMessageHeaders;
import org.apache.camel.CamelContext;
import org.junit.Test;
import org.springframework.http.HttpStatus;

/**
 * Citrus Integration test that test a Camel application.
 * We use Citrus as a HTTP client to send a message to a Camel application
 * that routes the messages to a JMS queue, which Citrus is simulating and
 * returning a reply message on the JMS reply queue, which Camel receives
 * and routes back to the calling HTTP client.
 * <p/>
 * In other words we have Citrus simulating a JMS backend, and as a client as well.
 * Notice how we can setup the test using the citrus designer.
 */
public class CitrusIT extends JUnit4CitrusTestDesigner {

    @Test
    @CitrusTest
    public void orderStatus() throws Exception {
        description("Checking order should be on hold");

        // random 5 digit order number
        variable("orderId", "citrus:randomNumber(5)");

        http().client("statusHttpClient")
                .get("/status?id=${orderId}")
                .contentType("text/xml").accept("text/xml")
                // use fork so we can continue with the test design (otherwise this would be a synchronous call)
                .fork(true);

        echo("Sent HTTP Request with orderId: ${orderId}");

        // the Camel application will call a JMS backend so lets use Citrus to simulate this
        // on the JMS queue we expect to receive the following message
        // and capture the JMS correlation ID so we can send back the correct reply message
        receive("statusEndpoint")
                .payload("<order><id>${orderId}</id></order>")
                .extractFromHeader(JmsMessageHeaders.CORRELATION_ID, "cid");

        // send back the JMS reply message that the order is done
        // and with the correct JMSCorrelationID
        send("statusEndpoint")
                .payload("<order><id>${orderId}</id><status>ON HOLD</status></order>")
                .header(JmsMessageHeaders.CORRELATION_ID, "${cid}");

        // the HTTP client is expected to receive a 200 OK message with the following XML structure
        http().client("statusHttpClient")
                .response(HttpStatus.OK)
                .payload("<order><id>${orderId}</id><status>ON HOLD</status></order>")
                .contentType("text/xml");

        // wait for Camel to shutdown nicely (citrus should have this out of the box in citrus-camel)
        waitForGracefulShutdown();
    }

    /**
     * Wait for graceful shutdown of Camel context before closing the test.
     */
    private void waitForGracefulShutdown() {
        waitFor().condition(new AbstractCondition() {
            @Override
            public boolean isSatisfied(TestContext context) {
                try {
                    context.getApplicationContext().getBean(CamelContext.class).stop();
                } catch (Exception e) {
                    return false;
                }

                return true;
            }

            @Override
            public String getSuccessMessage(TestContext context) {
                return "Successfully stopped Camel context";
            }

            @Override
            public String getErrorMessage(TestContext context) {
                return "Failed to stop Camel context";
            }
        });
    }

}
