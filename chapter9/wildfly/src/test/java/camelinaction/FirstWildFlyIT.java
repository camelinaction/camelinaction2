package camelinaction;

import java.io.File;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.hamcrest.CoreMatchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

/**
 * System test for testing against a managed WildFly container
 */
@RunWith(Arquillian.class)
public class FirstWildFlyIT {

    @Inject
    CamelContext camelContext;

    @Deployment
    public static WebArchive createDeployment() {
        // read the maven pom.xml and use all the runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        // build the .war with our source code and libraries
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "mycamel-wildfly.war");
        archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addPackages(true, "camelinaction");
        archive.addAsLibraries(files);
        return archive;
    }

    @Test
    public void testHello() throws Exception {
        // assume jboss is running
        String home = System.getenv("JBOSS_HOME");
        assumeThat("JBoss WildFly must be installed in JBOSS_HOME directory", home, CoreMatchers.anything(home));

        String out = camelContext.createProducerTemplate().requestBody("direct:hello", "Donald", String.class);
        assertEquals("Hello Donald", out);
    }

}