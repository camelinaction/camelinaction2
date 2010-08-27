import sbt._
import Process._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val akkaCamel = "se.scalablesolutions.akka" % "akka-camel_2.8.0" % "0.10" % "compile"
  val akkaSpring = "se.scalablesolutions.akka" % "akka-spring_2.8.0" % "0.10" % "compile"
  val camelJetty = "org.apache.camel" % "camel-jetty" % "2.4.0" % "compile"
  val camelSpring = "org.apache.camel" % "camel-spring" % "2.4.0" % "compile"

  override def repositories = Set("akka" at "http://scalablesolutions.se/akka/repository")
}
