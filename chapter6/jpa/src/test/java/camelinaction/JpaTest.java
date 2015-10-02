package camelinaction;

import java.util.List;
import javax.persistence.EntityManager;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jpa.JpaEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JpaTest extends CamelSpringTestSupport {

    @Test
    public void testRouteJpa() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMessageCount(1);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setName("motor");
        purchaseOrder.setAmount(1);
        purchaseOrder.setCustomer("honda");
        
        template.sendBody("seda:accounting", purchaseOrder);

        assertMockEndpointsSatisfied();
        assertEntityInDB();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                // use seda instead of JMS for testing
                from("seda:accounting")
                    .to("jpa:camelinaction.PurchaseOrder")
                    .to("mock:result");
            }
        };
    }

	private void assertEntityInDB() throws Exception {
        JpaEndpoint endpoint = context.getEndpoint("jpa:camelinaction.PurchaseOrder", JpaEndpoint.class);
        EntityManager em = endpoint.getEntityManagerFactory().createEntityManager();

        List list = em.createQuery("select x from camelinaction.PurchaseOrder x").getResultList();
        assertEquals(1, list.size());
        
        assertIsInstanceOf(PurchaseOrder.class, list.get(0));

        em.close();
    }
}
