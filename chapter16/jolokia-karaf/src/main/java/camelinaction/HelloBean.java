package camelinaction;

/**
 * A simple bean to say something
 */
public class HelloBean {

    private String say;

    public String getSay() {
        return say;
    }

    public void setSay(String say) {
        this.say = say;
    }

    public String hello() {
        return say;
    }
}
