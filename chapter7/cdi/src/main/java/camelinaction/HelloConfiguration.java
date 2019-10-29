package camelinaction;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import org.apache.camel.CamelContext;
import org.apache.camel.cdi.CdiCamelContext;
import org.apache.camel.spi.CamelEvent;

/**
 * CDI configuration of the hello application.
 */
@Singleton
public class HelloConfiguration {

    @Produces
    @ApplicationScoped
    public CamelContext createCamel() {
        // create CDI Camel and setup property placeholders
        CdiCamelContext cdi = new CdiCamelContext();
        cdi.getPropertiesComponent().setLocation("classpath:hello.properties");
        return cdi;
    }

    /**
     * Listen for event (observe using @Observes) when Camel is started.
     * <p/>
     * You can listen for any of the Camel events from org.apache.camel.management.event package.
     */
    void onContextStarted(@Observes CamelEvent.CamelContextStartedEvent event) {
        System.out.println("***************************************");
        System.out.println("* Camel started " + event.getContext().getName());
        System.out.println("***************************************");
    }
}
