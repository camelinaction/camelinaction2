package camelinaction

import org.springframework.context.support.ClassPathXmlApplicationContext

import akka.actor.Actor._
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE52 extends Application {
  import SampleActors._

  val appctx = new ClassPathXmlApplicationContext("/sample.xml")
  val consumer = actorOf[HttpConsumer1]

  for (service <- CamelServiceManager.service) service.awaitEndpointActivation(1) {
    consumer.start
  }

  for (template <- CamelContextManager.template) {
    val result = template.requestBody("http://localhost:8811/consumer1", "akka-spring rocks", classOf[String])
    assert(result == "received akka-spring rocks")
  }

  appctx.destroy
  consumer.stop
}