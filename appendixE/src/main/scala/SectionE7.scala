package camelinaction

import org.apache.camel.Exchange

import akka.actor.Actor._
import akka.actor.{Actor, ActorRef}
import akka.camel._

/**
 * @author Martin Krasser
 */
object SectionE7 extends Application {
  CamelServiceManager.startCamelService

  val httpTransformer = actorOf(new HttpTransformer).start
  val httpProducer = actorOf(new HttpProducer(httpTransformer)).start
  val httpConsumer = actorOf(new HttpConsumer(httpProducer)).start  
}

class HttpConsumer(producer: ActorRef) extends Actor with Consumer {
  def endpointUri = "jetty:http://0.0.0.0:8875/"

  protected def receive = {
    case msg => producer forward msg
  }
}

class HttpProducer(transformer: ActorRef) extends Actor with Producer {
  def endpointUri = "jetty:http://akkasource.org/?bridgeEndpoint=true"

  override protected def receiveBeforeProduce = {
    // only keep Exchange.HTTP_PATH message header
    case msg: Message => msg.setHeaders(msg.headers(Set(Exchange.HTTP_PATH)))
  }

  override protected def receiveAfterProduce = {
    // do not reply but forward result to transformer
    case msg => transformer forward msg
  }
}

class HttpTransformer extends Actor {
  protected def receive = {
    case msg: Failure => self.reply(msg)
    case msg: Message => self.reply(msg.transformBody { body: String =>
      body replaceAll ("Akka ", "AKKA ")
    })
  }
}
