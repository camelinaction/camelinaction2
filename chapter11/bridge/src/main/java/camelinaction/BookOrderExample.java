package camelinaction;

import org.apache.camel.BeanInject;
import org.apache.camel.CamelContext;
import org.apache.camel.Handler;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bean that orders some books by sending the orders using the Camel JPA endpoint.
 */
public class BookOrderExample {

    private final Logger log = LoggerFactory.getLogger(BookOrderExample.class);

    @BeanInject
    private ProducerTemplate template;

    @BeanInject
    private CamelContext context;

    @Handler
    public void orderSomeBooks() throws Exception {
        log.info("-------------------------------------------------------------------------------------------------------------------------");
        log.info("Make sure to have Postgres database up and running, as configured in the src/test/resources/META-INF/persistence.xml file");
        log.info("-------------------------------------------------------------------------------------------------------------------------");

        BookOrder order = new BookOrder();
        order.setAmount(1);
        order.setTitle("Camel in Action 2nd ed");

        template.sendBody("jpa:camelinaction.BookOrder", order);

        Thread.sleep(5000);
        log.info("... sleeping for 5 seconds and then stopping the route");

        // now stop the route
        context.stopRoute("books");

        // insert a new order which will sit in the database
        BookOrder order2 = new BookOrder();
        order2.setAmount(3);
        order2.setTitle("ActiveMQ in Action");

        template.sendBody("jpa:camelinaction.BookOrder", order2);

        log.info("-------------------------------------------------------------------------------------------------------------------------");
        log.info("Now we want to provoke a connection error, so stop the postgres database - and then press ENTER to continue!");
        log.info("-------------------------------------------------------------------------------------------------------------------------");

        System.console().readLine();

        context.startRoute("books");
        log.info("... starting route which should indicate some errors, which the bridge error handler should catch and handle");
        log.info("Notice that the consumer will backoff and not poll so fast, instead of every second, it now runs x10 sec.");
        log.info("Press CTRL+C to exit this application!");
    }

}
