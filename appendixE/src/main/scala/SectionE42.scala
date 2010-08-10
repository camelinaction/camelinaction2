package camelinaction

import org.springframework.context.support.ClassPathXmlApplicationContext

import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE42 extends Application {
  import SampleActors._

  val appctx = new ClassPathXmlApplicationContext("/sample.xml")
  val service = appctx.getBean("camelService", classOf[CamelService])

  val activation = service.expectEndpointActivationCount(1)
  val consumer = actorOf[HttpConsumer1].start

  activation.await

  val uri = "http://localhost:8811/consumer1"
  val result = CamelContextManager.template.requestBody(uri, "akka-spring rocks", classOf[String])

  assert(result == "received akka-spring rocks")

  consumer.stop
  appctx.destroy
}