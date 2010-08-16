package camelinaction

import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE21 extends Application {
  import CamelServiceManager._
  import SampleActors._

  startCamelService

  // expect one consumer endpoint to be activated (in the background)
  val activation = service.expectEndpointActivationCount(1)

  // start consumer (endpoint will be added to CamelContext asynchronously)
  val sedaConsumer = actorOf[SedaConsumer].start

  // wait for the endpoint being added to the CamelContext
  activation.await

  // will print 'message = hello akka' to stdout
  CamelContextManager.template.sendBody("seda:example", "hello akka-camel")

  // will stop the actor and the seda:example endpoint
  CamelContextManager.template.sendBody("seda:example", "stop")

  stopCamelService
}