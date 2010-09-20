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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author janstey
 * 
 */
public class MyInterceptor implements InterceptStrategy {
	private static final transient Log log = LogFactory
	        .getLog(MyInterceptor.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.camel.spi.InterceptStrategy#wrapProcessorInInterceptors(org
	 * .apache.camel.CamelContext, org.apache.camel.model.ProcessorDefinition,
	 * org.apache.camel.Processor, org.apache.camel.Processor)
	 */
	public Processor wrapProcessorInInterceptors(CamelContext context,
	        ProcessorDefinition<?> definition, final Processor target,
	        Processor nextTarget) throws Exception {
		// to make the Default channel happy
		return new DelegateAsyncProcessor(new Processor() {

			public void process(Exchange exchange) throws Exception {
				log.info("Before the processor...");
				target.process(exchange);
				log.info("After the processor...");
			}
		});
	}

}
