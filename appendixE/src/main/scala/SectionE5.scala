package camelinaction 

import java.net.ConnectException

import org.apache.camel.builder.RouteBuilder

import se.scalablesolutions.akka.actor.Actor._
import se.scalablesolutions.akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE5 extends Application {
  import SampleActors._

  val producer = actorOf[HttpProducer1].start

  CamelService.start
  CamelContextManager.context.addRoutes(new CustomRoute(producer.uuid))
  CamelContextManager.template.requestBody("direct:test", "feel good", classOf[String]) match {
    case "<received>feel good</received>" => println("communication ok")
    case "feel bad"                       => println("communication failed")
    case _                                => println("unexpected response")
  }

  CamelService.stop
  producer.stop
}

class CustomRoute(uuid: String) extends RouteBuilder {
  def configure = {
    from("direct:test")
    .onException(classOf[ConnectException])
      .handled(true)
      .transform.constant("feel bad")
      .end
    .to("actor:uuid:%s" format uuid)
  }
}
