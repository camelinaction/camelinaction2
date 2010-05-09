/**
 * 
 */
package camelinaction.interceptor;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.InterceptStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author janstey
 *
 */
public class MyInterceptor implements InterceptStrategy {

    private static final transient Log log 
    = LogFactory.getLog(MyInterceptor.class);

    public Processor wrapProcessorInInterceptors(
        CamelContext context,                   
        ProcessorDefinition<?> definition,      
        final Processor target,                 
        Processor nextTarget)                   
        throws Exception {
        
        if (definition.getShortName() == "wireTap") {
            return target;
        } else {
            return new Processor() {
        
                public void process(Exchange exchange) throws Exception {
                    log.info("Entering the processor...");
                    target.process(exchange);              
                    log.info("Exiting the processor...");
                }
            };
        }    
    }
}
