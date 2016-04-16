package camelinaction;

import java.io.File;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
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
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.maven;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

/**
 * Unit test to test this example running in Apache Karaf using Pax-Exam for testing on the container.
 */
@RunWith(PaxExam.class)
public class PaxExamTest {

    private static final Logger LOG = LoggerFactory.getLogger(PaxExamTest.class);

    @Inject
    protected BundleContext bundleContext;

    @Inject
    @Filter("(camel.context.name=quotesCamel)")
    protected CamelContext camelContext;

    @Test
    public void testPaxExam() throws Exception {
        // we should have completed 0 exchange
        long total = camelContext.getManagedCamelContext().getExchangesTotal();
        assertEquals("There should have been 0 exchanges completed now", 0, total);

        String json = camelContext.createProducerTemplate().requestBody("http://localhost:8181/camel/say", null, String.class);
        System.out.println("Wiseman says: " + json);
        LOG.info("Wiseman says: {}", json);

        // should be one of the quotes
        boolean found = false;
        for (String quote : Quotes.QUOTES) {
            if (json.contains(quote)) {
                found = true;
            }
        }
        assertTrue("Response should be a quote, was: " + json, found);

        // and we should have completed 1 exchange
        total = camelContext.getManagedCamelContext().getExchangesTotal();
        assertEquals("There should have been 1 exchanges completed now", 1, total);
    }

    @Configuration
    public Option[] config() {
        return new Option[]{
                karafDistributionConfiguration()
                        .frameworkUrl(maven().groupId("org.apache.karaf").artifactId("apache-karaf").version("4.0.4").type("tar.gz"))
                        .karafVersion("4.0.4")
                        .name("Apache Karaf")
                        .useDeployFolder(false)
                        .unpackDirectory(new File("target/karaf")),

                // keep the folder so we can look inside when something fails
                keepRuntimeFolder(),

                // Disable the SSH port
                configureConsole().ignoreRemoteShell(),

                // Configure Logging
                logLevel(LogLevelOption.LogLevel.WARN),

                // Install JUnit
                junitBundles(),

                // Install base camel features
                features(getCamelKarafFeatureUrl(), "camel", "camel-test"),

                // and use camel-http for testing
                features(getCamelKarafFeatureUrl(), "camel-http"),

                // install our example feature
                features(maven().groupId("com.camelinaction").artifactId("chapter9-pax-exam").version("2.0.0").classifier("features").type("xml"), "camel-quote")
        };
    }

    public static UrlReference getCamelKarafFeatureUrl() {
        return mavenBundle().
                groupId("org.apache.camel.karaf").
                artifactId("apache-camel")
                .version("2.17.0")
                .classifier("features")
                .type("xml");
    }

}
