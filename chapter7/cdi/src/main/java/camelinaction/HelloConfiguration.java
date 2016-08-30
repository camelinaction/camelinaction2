package camelinaction;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.management.event.CamelContextStartedEvent;

/**
 * CDI configuration of the hello application.
 */
@Singleton
public class HelloConfiguration {

    /**
     * Create the Camel properties component using CDI @Produces with the name: properties
     */
    @Produces
    @Named("properties")
    PropertiesComponent propertiesComponent() {
        PropertiesComponent component = new PropertiesComponent();
        // load properties file form the classpath
        component.setLocation("classpath:hello.properties");
        return component;
    }

    /**
     * Listen for event (observe using @Observes) when Camel is started.
     * <p/>
     * You can listen for any of the Camel events from org.apache.camel.management.event package.
     */
    void onContextStarted(@Observes CamelContextStartedEvent event) {
        System.out.println("***************************************");
        System.out.println("* Camel started " + event.getContext().getName());
        System.out.println("***************************************");
    }
}
