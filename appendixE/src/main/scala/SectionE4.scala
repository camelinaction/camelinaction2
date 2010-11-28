package camelinaction

import akka.actor.Actor._
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE4 extends Application {
  import SampleActors._

  val service = CamelServiceManager.startCamelService
  val httpConsumer2 = actorOf[HttpConsumer2]
  val httpProducer1 = actorOf[HttpProducer1].start
  val httpProducer2 = actorOf[HttpProducer2].start

  service.awaitEndpointActivation(1) {
    httpConsumer2.start
  }

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

  service.stop
  httpConsumer2.stop
  httpProducer1.stop
  httpProducer2.stop
}