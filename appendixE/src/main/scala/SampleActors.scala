package camelinaction

import akka.actor.Actor
import akka.camel._

/**
 * @author Martin Krasser
 */
object SampleActors {
  // ------------------------------------------------------------------
  //  Plain actors
  // ------------------------------------------------------------------

  class SimpleActor extends Actor {
    protected def receive = {
      case "stop" => self.stop
      case msg    => println("message = %s" format msg)
    }
  }

  // ------------------------------------------------------------------
  //  Consumer actors
  // ------------------------------------------------------------------

  class SedaConsumer extends Actor with Consumer {
    def endpointUri = "seda:example"
    protected def receive = {
      case Message("stop", headers) => self.stop
      case Message(body, headers)   => println("message = %s" format body)
    }
  }

  class HttpConsumer1 extends Actor with Consumer {
    def endpointUri = "jetty:http://0.0.0.0:8811/consumer1"

    protected def receive = {
      case msg: Message => self.reply("received %s" format msg.bodyAs[String])
    }
  }

  class HttpConsumer2 extends Actor with Consumer {
    def endpointUri = "jetty:http://0.0.0.0:8811/consumer2"

    protected def receive = {
      case msg: Message => {
        val body = "<received>%s</received>" format msg.bodyAs[String]
        val headers = Map("Content-Type" -> "application/xml")
        self.reply(Message(body, headers))
      }
    }
  }

  // ------------------------------------------------------------------
  //  Producer actors
  // ------------------------------------------------------------------

  class HttpProducer1 extends Actor with Producer {
    def endpointUri = "http://localhost:8811/consumer2"
  }

  class HttpProducer2 extends Actor with Producer {
    def endpointUri = "http://localhost:8811/consumer3"

    override protected def receiveAfterProduce = {
      case m: Message => println("response = %s" format m.bodyAs[String])
      case f: Failure => println("failure = %s" format f.cause.getMessage)
    }
  }

  class JmsProducer extends Actor with Producer with Oneway {
    def endpointUri = "jms:queue:test"
  }
}