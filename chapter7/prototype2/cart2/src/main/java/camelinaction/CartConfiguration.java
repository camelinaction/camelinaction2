package camelinaction;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.camel.component.properties.PropertiesComponent;

/**
 * Configures the application.
 */
@Singleton
public class CartConfiguration {

    /**
     * Create the Camel properties component using CDI @Produces with the name: properties
     */
    @Produces
    @Named("properties")
    PropertiesComponent propertiesComponent() {
        PropertiesComponent component = new PropertiesComponent();
        // load properties file form the classpath
        component.setLocation("classpath:cart.properties");
        return component;
    }

}
