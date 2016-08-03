package camelinaction.api;

/**
 * Sample API used by HiWorld Component whose method signatures are read from Javadoc.
 */
public class HiWorldJavadocHello {

    public String sayHi() {
        return "Hello!";
    }

    public String greetMe(String name) {
        return "Hello " + name;
    }

    public String greetUs(String name1, String name2) {
            return "Hello " + name1 + ", " + name2;
    }
}
