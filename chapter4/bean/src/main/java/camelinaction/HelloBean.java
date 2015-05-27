package camelinaction;

/**
 * A simple bean to print hello
 */
public class HelloBean {

    public String hello(String name) {
        System.out.println("Invoking HelloBean with " + name);
        return "Hello " + name;
    }

}
