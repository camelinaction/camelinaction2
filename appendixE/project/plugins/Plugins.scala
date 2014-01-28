import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val AkkaRepo = "Akka Repository" at "http://akka.io/repository/"
  val AkkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "1.0"
}

