package camelinaction

import org.apache.camel.{Exchange, Processor}
import org.apache.camel.ExchangePattern._

import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE32 extends Application {
  import CamelServiceManager._
  import SampleActors._

  startCamelService

  // expect two consumer endpoints to be activated (in the background)
  val activation = service.expectEndpointActivationCount(2)

  // start consumers (endpoints will be added to CamelContext asynchronously)
  val httpConsumer1 = actorOf[HttpConsumer1].start
  val httpConsumer2 = actorOf[HttpConsumer2].start

  // wait for the endpoints being added to the CamelContext
  activation.await

  // in-out message exchange with HttpConsumer1
  val exchange1 = CamelContextManager.template.send("http://localhost:8811/consumer1", InOut, new Processor {
    def process(exchange: Exchange) = exchange.getIn.setBody("Akka rocks")
  })

  // in-out message exchange with HttpConsumer2
  val exchange2 = CamelContextManager.template.send("http://localhost:8811/consumer2", InOut, new Processor {
    def process(exchange: Exchange) = exchange.getIn.setBody("Akka rocks")
  })

  assert("received Akka rocks"             == exchange1.getOut.getBody(classOf[String]))
  assert("<received>Akka rocks</received>" == exchange2.getOut.getBody(classOf[String]))
  assert("application/xml"                 == exchange2.getOut.getHeader("Content-Type"))

  stopCamelService

  httpConsumer1.stop
  httpConsumer2.stop
}
