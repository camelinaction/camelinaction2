package camelinaction;

import java.io.File;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.options.UrlReference;
import org.ops4j.pax.exam.util.Filter;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

/**
 * Integration test to test this example running in Apache Karaf using Pax-Exam for testing on the container.
 */
@RunWith(PaxExam.class)
public class PaxExamIT {

    private static final Logger LOG = LoggerFactory.getLogger(PaxExamIT.class);

    @Inject
    protected BundleContext bundleContext;

    // inject the Camel application with this name (eg the id attribute in <camelContext>)
    @Inject
    @Filter("(camel.context.name=quotesCamel)")
    protected CamelContext camelContext;

    @Test
    public void testPaxExam() throws Exception {
        // we should have completed 0 exchange
        long total = camelContext.getManagedCamelContext().getExchangesTotal();
        assertEquals("Should be 0 exchanges completed", 0, total);

        // need a little delay to be ready
        Thread.sleep(2000);

        // call the servlet, and log what it returns
        String url = "http4://localhost:8181/camel/say";
        ProducerTemplate template = camelContext.createProducerTemplate();
        String json = template.requestBody(url, null, String.class);
        System.out.println("Wiseman says: " + json);
        LOG.info("Wiseman says: {}", json);

        // and we should have completed 1 exchange
        total = camelContext.getManagedCamelContext().getExchangesTotal();
        assertEquals("Should be 1 exchanges completed", 1, total);
    }

    @Configuration
    public Option[] config() {
        return new Option[]{
                // setup which karaf server we are using
                karafDistributionConfiguration()
                        .frameworkUrl(maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("4.1.3").type("tar.gz"))
                        .karafVersion("4.1.3")
                        .name("Apache Karaf")
                        .useDeployFolder(false)
                        .unpackDirectory(new File("target/karaf")),

                // keep the folder so we can look inside when something fails, eg check the data/logs directory
                keepRuntimeFolder(),

                // Disable the SSH port
                configureConsole().ignoreRemoteShell(),

                // Configure Logging to not be verbose, if you set to DEBUG you see a lot of details
                logLevel(LogLevelOption.LogLevel.WARN),

                // Install JUnit
                junitBundles(),

                // Install base camel features
                features(getCamelKarafFeatureUrl(), "camel", "camel-test"),

                // and use camel-http for testing
                features(getCamelKarafFeatureUrl(), "camel-http4"),

                // install our example feature
                features(maven().groupId("com.camelinaction").artifactId("chapter9-pax-exam").version("2.0.0").classifier("features").type("xml"), "camel-quote")
        };
    }

    public static UrlReference getCamelKarafFeatureUrl() {
        // the Apache Camel Karaf feature file
        return mavenBundle().
                groupId("org.apache.camel.karaf").
                artifactId("apache-camel")
                .version("2.19.0")
                .classifier("features")
                .type("xml");
    }

}
