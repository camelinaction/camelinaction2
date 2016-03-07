package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

public class OrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // configure rest-dsl
        restConfiguration()
           // to use undertow component and run on port 8080
            .component("undertow").port(8080)
            // and enable json/xml binding mode
            .bindingMode(RestBindingMode.json_xml)
            // lets enable pretty printing json responses
            .dataFormatProperty("prettyPrint", "true");

        // rest services under the orders context-path
        rest("/orders")
            // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
            .get("{id}").outType(Order.class)
                .to("bean:orderService?method=getOrder(${header.id})")
                // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
            .post().type(Order.class)
                .to("bean:orderService?method=createOrder")
                // need to specify the POJO types the binding is using (otherwise json binding defaults to Map based)
            .put().type(Order.class)
                .to("bean:orderService?method=updateOrder")
            .delete("{id}")
                .to("bean:orderService?method=cancelOrder(${header.id})");
    }
}
