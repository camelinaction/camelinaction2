package camelinaction

import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE4 extends Application {
  import CamelServiceManager._
  import SampleActors._

  startCamelService

  val activation = service.expectEndpointActivationCount(1)
  val httpConsumer2 = actorOf[HttpConsumer2].start

  activation.await

  val httpProducer1 = actorOf[HttpProducer1].start
  val httpProducer2 = actorOf[HttpProducer2].start

  // Send message to httpProducer and wait for response
  httpProducer1 !! "Camel rocks" match {
    case Some(m: Message) => println("response = %s" format m.bodyAs[String])
    case Some(f: Failure) => println("failure = %s" format f.cause.getMessage)
    case None             => println("timeout")
  }

  // Send message to httpProducer2 without waiting for a response
  httpProducer2 ! "Camel rocks"

  // Wait a bit for httpProducer2 to write the response to stdout
  Thread.sleep(2000)

  stopCamelService
  httpProducer1.stop
  httpProducer2.stop
  httpConsumer2.stop
}