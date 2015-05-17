package camelinaction;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The main class which starts this example.
 */
public class BookOrderMain {

    private BookCamel camel;

    public static void main(String[] args) throws Exception {
        BookOrderMain me = new BookOrderMain();
        me.boot();
    }

    public void boot() throws Exception {
        camel = new BookCamel();
        camel.setApplicationContext(new ClassPathXmlApplicationContext("camelinaction/camel-bridge.xml"));
        camel.enableHangupSupport();
        camel.run();
    }
}
