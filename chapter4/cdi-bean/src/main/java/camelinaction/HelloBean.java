package camelinaction;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * A simple bean to print hello.
 * <p/>
 * The bean is named using the CDI annotation @Named
 */
@Singleton
@Named("helloBean")
public class HelloBean {

    private int counter;

    public String hello() {
        return "Hello " + ++counter + " times";
    }

}
