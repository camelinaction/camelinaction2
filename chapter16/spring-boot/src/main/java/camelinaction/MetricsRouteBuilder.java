package camelinaction;

import org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicyFactory;
import org.apache.camel.spring.boot.FatJarRouter;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricsRouteBuilder extends FatJarRouter {

    @Override
    public void configure() throws Exception {
        // add dropwizard metrics to all our routes
        getContext().addRoutePolicyFactory(new MetricsRoutePolicyFactory());

        from("timer:foo").id("foo")
            .delay(simple("${random(0,1000)}"))
            .log("Foo is done");

        from("timer:bar").id("bar")
            .delay(simple("${random(0,5000)}"))
            .log("Bar is done");
    }
}
