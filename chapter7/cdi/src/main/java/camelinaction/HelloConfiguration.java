package camelinaction;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.camel.component.properties.PropertiesComponent;

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
}
