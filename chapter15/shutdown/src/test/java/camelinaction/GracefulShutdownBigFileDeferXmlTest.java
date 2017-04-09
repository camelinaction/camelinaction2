package camelinaction;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class GracefulShutdownBigFileDeferXmlTest extends GracefulShutdownBigFileDeferTest {

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-route-defer-xml.xml");
    }

}
