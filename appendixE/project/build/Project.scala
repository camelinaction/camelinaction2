import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  val AkkaRepo = "Akka Repository" at "http://scalablesolutions.se/akka/repository"

  val akkaCamel = akkaModule("camel")
  val akkaKernel = akkaModule("spring")

  val camelJetty = "org.apache.camel" % "camel-jetty" % "2.5.0" % "compile"
  val camelSpring = "org.apache.camel" % "camel-spring" % "2.5.0" % "compile"
  val log4jOverSlf4j = "org.slf4j" % "log4j-over-slf4j" % "1.6.1" % "runtime"
}
