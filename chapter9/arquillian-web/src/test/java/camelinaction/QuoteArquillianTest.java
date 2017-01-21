package camelinaction;

import java.net.URL;
import java.nio.file.Paths;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.collection.IsIn.isIn;

@RunWith(Arquillian.class)
public class QuoteArquillianTest {

    /**
     * Build a deployment archive
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        // this is a WAR project so use WebArchive
        return ShrinkWrap.create(WebArchive.class)
                // add our class
                .addClass(Quotes.class)
                // add the web.xml
                .setWebXML(Paths.get("src/main/webapp/WEB-INF/web.xml").toFile());
    }

    /**
     * Test method that runs as a client
     */
    @Test
    @RunAsClient
    public void testQuote(@ArquillianResource URL url) throws Exception {
        // the URL is the URL to the web application that was deployed

        // use Rest Assured to define a test case
        given().
            baseUri(url.toString()).
        when().
            get("/say").
        then().
            body("quote", isIn(Quotes.QUOTES));
    }

}
