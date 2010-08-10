package camelinaction

import se.scalablesolutions.akka.actor.Actor._

/**
 * @author Martin Krasser
 */
object SectionE1 extends Application {
  import SampleActors._

  val actor = actorOf[SimpleActor].start

  // will print 'message = hello akka' to stdout
  actor ! "hello akka"

  // will stop the actor
  actor ! "stop"
}