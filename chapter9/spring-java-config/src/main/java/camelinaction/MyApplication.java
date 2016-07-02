package camelinaction;

import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.apache.camel.spring.javaconfig.Main;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The main class that holds the Spring Java Configuration configuration
 * <p/>
 * To automatic add the Camel routes then enable component scan in the package name <tt>camelinaction</tt>
 */
@Configuration
@ComponentScan("camelinaction")
public class MyApplication extends CamelConfiguration {

    /**
     * A main class to run this application as a plain static void main application
     */
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        // configure the name of the @Configuration class
        main.setConfigClass(MyApplication.class);
        main.run();
    }

}
