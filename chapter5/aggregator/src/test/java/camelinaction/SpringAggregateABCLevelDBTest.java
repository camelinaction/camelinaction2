package camelinaction;

import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The ABC example for using persistent Aggregator EIP using Spring XML.
 * <p/>
 * See {@link AggregateABCLevelDBTest} for details.
 */
public class SpringAggregateABCLevelDBTest extends CamelSpringTestSupport {

    @Test
    public void testABCLevelDB() throws Exception {
        System.out.println("Copy 3 files to target/inbox to trigger the completion");
        System.out.println("Files to copy:");
        System.out.println("  copy src/test/resources/a.txt target/inbox");
        System.out.println("  copy src/test/resources/b.txt target/inbox");
        System.out.println("  copy src/test/resources/c.txt target/inbox");
        System.out.println("\nSleeping for 20 seconds");
        System.out.println("You can let the test terminate (or press ctrl +c) and then start it again");
        System.out.println("Which should let you be able to resume.");

        Thread.sleep(20 * 1000);
    }

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/aggregate-abc-leveldb.xml");
    }
}
