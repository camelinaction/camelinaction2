package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class ReverseDataFormatTest extends CamelTestSupport {

  @Test
  public void testMarshalAndUnmarshalMap() throws Exception {
      String in = "Test String";
      MockEndpoint mock = getMockEndpoint("mock:reverse");
      mock.message(0).body().isEqualTo(in);

      Object marshalled = template.requestBody("direct:in", in);
      template.sendBody("direct:back", marshalled);
      mock.assertIsSatisfied();
  }
  
  @Override
  protected RouteBuilder createRouteBuilder() throws Exception {
      return new RouteBuilder() {

          @Override
          public void configure() throws Exception {
              DataFormat format = new ReverseDataFormat();
              from("direct:in").marshal(format);
              from("direct:back").unmarshal(format).to("mock:reverse");
          }
      };
  }
}