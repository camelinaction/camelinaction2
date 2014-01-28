package camelinaction

import akka.actor.Actor._
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE31 extends Application {
  import SampleActors._

  val service = CamelServiceManager.startCamelService

  service.awaitEndpointActivation(1) {
    actorOf[SedaConsumer].start
  }

  for (template <- CamelContextManager.template) {
    // will print 'message = hello akka' to stdout
    template.sendBody("seda:example", "hello akka-camel")


    service.awaitEndpointDeactivation(1) {
      // will stop the actor and the seda:example endpoint
      template.sendBody("seda:example", "stop")
    }
  }

  service.stop
}