package camelinaction;

import java.io.File;
import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * System test for testing against a managed wildfly container
 */
@RunWith(Arquillian.class)
public class FirstWildFlyTest {

    @Inject
    CamelContext camelContext;

    @Deployment
    public static WebArchive createDeployment() {
        // read the maven pom.xml and use all the runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        // build the .war with our source code and libraries
        final WebArchive archive = ShrinkWrap.create(WebArchive.class, "beginner-wildfly.war");
        archive.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        archive.addPackages(true, "camelinaction");
        archive.addAsLibraries(files);
        return archive;
    }

    @Test
    public void testHello() throws Exception {
        String out = camelContext.createProducerTemplate().requestBody("direct:hello", "Donald", String.class);
        assertEquals("Hello Donald", out);
    }

}