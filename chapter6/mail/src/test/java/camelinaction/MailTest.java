package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MailTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            
            @Override
            public void configure() throws Exception {
                from("imap://claus@localhost?password=secret").to("mock:result");
            }
        };
    }

    @Test
    public void testSendAndRecieveMail() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Yes, Camel rocks!");

        template.sendBody("smtp://jon@localhost?password=secret&to=claus@localhost", "Yes, Camel rocks!");
        
        mock.assertIsSatisfied();
    }

    @Test
    public void testSendAndRecieveMailWithSubjectHeader() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Yes, Camel rocks!");

        template.sendBodyAndHeader("smtp://jon@localhost?password=secret&to=claus@localhost", "Yes, Camel rocks!", 
           "subject", "Does Camel rock?");

        mock.assertIsSatisfied();
    }


}
