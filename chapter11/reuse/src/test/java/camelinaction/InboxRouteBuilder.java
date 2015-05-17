package camelinaction;

public class InboxRouteBuilder extends BaseRouteBuilder {

    @Override
    public void configure() throws Exception {
        // must call super to reuse the common error handler
        super.configure();

        from("file://target/orders?delay=10000")
                .beanRef("orderService", "toCsv")
                .to("mock:file")
                .to("seda:queue.inbox");
    }
}
