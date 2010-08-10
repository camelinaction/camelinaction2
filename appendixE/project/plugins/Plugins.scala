import sbt._

class Plugins(info: ProjectInfo) extends PluginDefinition(info) {
  val bumRepo = "Bum Networks Release Repository" at "http://repo.bumnetworks.com/releases"
  val akkaPlugin = "se.scalablesolutions.akka" % "akka-sbt-plugin" % "0.10"
}
