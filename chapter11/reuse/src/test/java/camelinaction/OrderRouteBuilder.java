package camelinaction;

public class OrderRouteBuilder extends BaseRouteBuilder {

    @Override
    public void configure() throws Exception {
        // must call super to reuse the common error handler
        super.configure();

        // route to process the order
        from("seda:queue.inbox")
            .beanRef("orderService", "validate")
            .beanRef("orderService", "enrich")
            .to("mock:queue.order");

    }
}
