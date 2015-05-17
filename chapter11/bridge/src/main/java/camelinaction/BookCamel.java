package camelinaction;

import org.apache.camel.spring.Main;

/**
 * The Camel main which we use the embed and run Camel, and then we override the {@link #afterStart()}
 * method as we want to send some book orders using the {@link BookOrderExample} bean.
 */
public class BookCamel extends Main {

    @Override
    protected void afterStart() throws Exception {
        BookOrderExample example = getCamelContexts().get(0).getRegistry().lookupByNameAndType("bookOrderExample", BookOrderExample.class);
        example.orderSomeBooks();
    }
}
