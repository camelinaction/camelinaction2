/**
 * 
 */
package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A custom interceptor that logs before and after invoking a Camel {@link Processor}
 */
public class MyInterceptor implements InterceptStrategy {

	private static final Logger LOG = LoggerFactory.getLogger(MyInterceptor.class);

	public Processor wrapProcessorInInterceptors(CamelContext context,
	        ProcessorDefinition<?> definition, final Processor target,
	        Processor nextTarget) throws Exception {

		return new DelegateAsyncProcessor(new Processor() {

			public void process(Exchange exchange) throws Exception {
				LOG.info("Before the processor...");
				target.process(exchange);
				LOG.info("After the processor...");
			}
		});
	}

}
