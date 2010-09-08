import sbt._

class Project(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  val akkaCamel = akkaModule("camel")
  val akkaKernel = akkaModule("spring")

  val camelJetty = "org.apache.camel" % "camel-jetty" % "2.4.0" % "compile"
  val camelSpring = "org.apache.camel" % "camel-spring" % "2.4.0" % "compile"
  val log4jOverSlf4j = "org.slf4j" % "log4j-over-slf4j" % "1.6.0" % "runtime"

  // Temporary workaround to ignore logback.xml in akka-core_2.8.0-0.10.jar
  System.setProperty("logback.configurationFile", "src/main/resources/logback.xml")
}
