import sbt._
import Process._

class Project(info: ProjectInfo) extends DefaultProject(info) {
  val akkaCamel = "se.scalablesolutions.akka" % "akka-camel_2.8.0" % "0.10" % "compile"
  val akkaSpring = "se.scalablesolutions.akka" % "akka-spring_2.8.0" % "0.10" % "compile"
  val camelJetty = "org.apache.camel" % "camel-jetty" % "2.4.0" % "compile"
  val camelSpring = "org.apache.camel" % "camel-spring" % "2.4.0" % "compile"
  val log4j = "log4j" % "log4j" % "1.2.14" % "runtime"

  override def repositories = Set("akka" at "http://scalablesolutions.se/akka/repository")

  // Temporary workaround to ignore logback.xml in akka-core_2.8.0-0.10.jar
  System.setProperty("logback.configurationFile", "src/main/resources/logback.xml")
}
