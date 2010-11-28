package camelinaction

import org.apache.camel.{Exchange, Processor}
import org.apache.camel.ExchangePattern._

import akka.actor.Actor._
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE32 extends Application {
  import SampleActors._

  val service = CamelServiceManager.startCamelService
  val httpConsumer1 = actorOf[HttpConsumer1]
  val httpConsumer2 = actorOf[HttpConsumer2]

  service.awaitEndpointActivation(2) {
    httpConsumer1.start
    httpConsumer2.start
  }

  for (template <- CamelContextManager.template) {
    // in-out message exchange with HttpConsumer1
    val exchange1 = template.send("http://localhost:8811/consumer1", InOut, new Processor {
      def process(exchange: Exchange) = exchange.getIn.setBody("Akka rocks")
    })

    // in-out message exchange with HttpConsumer2
    val exchange2 = template.send("http://localhost:8811/consumer2", InOut, new Processor {
      def process(exchange: Exchange) = exchange.getIn.setBody("Akka rocks")
    })

    assert("received Akka rocks"             == exchange1.getOut.getBody(classOf[String]))
    assert("<received>Akka rocks</received>" == exchange2.getOut.getBody(classOf[String]))
    assert("application/xml"                 == exchange2.getOut.getHeader("Content-Type"))
  }

  service.stop
  httpConsumer1.stop
  httpConsumer2.stop
}
