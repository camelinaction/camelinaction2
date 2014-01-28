package camelinaction 

import java.net.ConnectException

import org.apache.camel.builder.RouteBuilder

import akka.actor.Actor._
import akka.actor.Uuid
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE6 extends Application {
  import SampleActors._

  val service = CamelServiceManager.startCamelService
  val producer = actorOf[HttpProducer1].start

  for (context  <- CamelContextManager.context;
       template <- CamelContextManager.template) {
    context.addRoutes(new CustomRoute(producer.uuid))
    template.requestBody("direct:test", "feel good", classOf[String]) match {
      case "<received>feel good</received>" => println("communication ok")
      case "feel bad"                       => println("communication failed")
      case _                                => println("unexpected response")
    }
  }

  service.stop
  producer.stop
}

class CustomRoute(uuid: Uuid) extends RouteBuilder {
  def configure = {
    from("direct:test")
    .onException(classOf[ConnectException])
      .handled(true)
      .transform.constant("feel bad")
      .end
    .to("actor:uuid:%s" format uuid)
  }
}
