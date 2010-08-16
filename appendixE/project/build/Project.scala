import sbt._
import Process._

class Project(info: ProjectInfo) extends DefaultProject(info) with AkkaProject {
  val akkaCamel = akkaModule("camel")
  val akkaSpring = akkaModule("spring")
  
  val camelJetty = "org.apache.camel" % "camel-jetty" % "2.4.0" % "compile"
  val camelSpring = "org.apache.camel" % "camel-spring" % "2.4.0" % "compile"
  val slf4jApi = "org.slf4j" % "slf4j-api" % "1.6.0" % "compile"
  val junit = "junit" % "junit" % "4.8.1" % "test->default"
  val scalatest = "org.scalatest" % "scalatest" % "1.2-for-scala-2.8.0.final-SNAPSHOT" % "test->default"

  override def repositories = Set( "scala-tools-snapshots" at "http://scala-tools.org/repo-snapshots/")
}
