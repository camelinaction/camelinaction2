package camelinaction;

import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.codahale.metrics.MetricRegistry;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.metrics.routepolicy.MetricsRegistryService;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MetricsTest extends CamelTestSupport {

    @Override
    protected boolean useJmx() {
        return true;
    }

    @Test
    public void testMetrics() throws Exception {
        // wait for 20 or more to be done
        NotifyBuilder builder = new NotifyBuilder(context).whenDone(20).create();
        builder.matches(1, TimeUnit.MINUTES);

        context.stopRoute("foo");
        context.stopRoute("bar");

        // you can get access to dropwizard metrics from Java API as well
        MetricsRegistryService registryService = context.hasService(MetricsRegistryService.class);
        if (registryService != null) {
            MetricRegistry registry = registryService.getMetricsRegistry();

            long count = registry.timer("camel-1:foo.responses").getCount();
            double mean = registry.timer("camel-1:foo.responses").getMeanRate();
            double rate1 = registry.timer("camel-1:foo.responses").getOneMinuteRate();
            double rate5 = registry.timer("camel-1:foo.responses").getFiveMinuteRate();
            double rate15 = registry.timer("camel-1:foo.responses").getFifteenMinuteRate();
            log.info("Foo metrics: count={}, mean={}, rate1={}, rate5={}, rate15={}", count, mean, rate1, rate5, rate15);

            count = registry.timer("camel-1:bar.responses").getCount();
            mean = registry.timer("camel-1:bar.responses").getMeanRate();
            rate1 = registry.timer("camel-1:bar.responses").getOneMinuteRate();
            rate5 = registry.timer("camel-1:bar.responses").getFiveMinuteRate();
            rate15 = registry.timer("camel-1:bar.responses").getFifteenMinuteRate();
            log.info("Bar metrics: count={}, mean={}, rate1={}, rate5={}, rate15={}", count, mean, rate1, rate5, rate15);
        }

        // and we can also access the JMX MBean to dump the statistics in JSon
        ObjectName on = new ObjectName("org.apache.camel:context=camel-1,type=services,name=MetricsRegistryService");
        MBeanServer server = context.getManagementStrategy().getManagementAgent().getMBeanServer();
        String json = (String) server.invoke(on, "dumpStatisticsAsJson", null, null);
        log.info("MetricsRegistryService.dumpStatisticsAsJson() => \n{}", json);
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MetricsRouteBuilder();
    }
}
