package camelinaction;

import com.consol.citrus.dsl.endpoint.CitrusEndpoints;
import com.consol.citrus.http.client.HttpClient;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.xbean.XBeanBrokerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;

import java.net.URI;

/**
 * Citrus Spring configuration for JMS backend and Http client components
 * that are used during the integration test.
 *
 * This configuration also imports the Camel application as Spring context.
 */
@Configuration
@ImportResource("classpath:camelinaction/status.xml")
@PropertySource("classpath:citrus.properties")
public class EndpointConfig {

    /** ActiveMQ broker url */
    @Value("${jms.broker.url}")
    private String jmsBrokerUrl;

    /**
     * Citrus JMS endpoint
     * do a sync request/reply over JMS on the queue named order.status with a timeout of 10 seconds
     */
    @Bean
    public JmsEndpoint statusEndpoint() {
        return CitrusEndpoints.jms().synchronous()
            .connectionFactory(jmsConnectionFactory())
            .destination("order.status")
            .timeout(10000L)
            .build();
    }

    /**
     * Citrus Http client
     * do a sync request/reply over HTTP on localhost:8080/order with a GET call and timeout for 60 seconds
     */
    @Bean
    public HttpClient statusHttpClient() {
        return CitrusEndpoints.http()
                .client()
                .requestUrl("http://localhost:8080/order")
                .requestMethod(HttpMethod.GET)
                .contentType("text/xml")
                .timeout(60000L)
                .build();
    }

    /**
     * ActiveMQ jms connection factory depends on embedded message broker.
     * Setup the JMS connection factory to use for integration test
     * we will embed an ActiveMQ broker
     */
    @Bean
    @DependsOn("jmsMessageBroker")
    public ActiveMQConnectionFactory jmsConnectionFactory() {
        return new ActiveMQConnectionFactory(jmsBrokerUrl);
    }

    /**
     * ActiveMQ message broker as embedded service.
     */
    @Bean
    public BrokerService jmsMessageBroker() throws Exception {
        XBeanBrokerService messageBroker = new XBeanBrokerService ();

        TransportConnector connector = new TransportConnector();
        connector.setUri(new URI(jmsBrokerUrl));
        messageBroker.addConnector(connector);

        messageBroker.setStart(true);
        messageBroker.setUseJmx(false);
        messageBroker.setPersistent(false);

        return messageBroker;
    }
}
