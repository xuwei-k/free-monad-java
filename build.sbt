val commonSettings = Seq(
  scalaVersion := "2.11.1",
  scalacOptions ++= Seq("-Xexperimental", "-deprecation"),
  javacOptions += "-Xlint:unchecked",
  licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))
)

commonSettings

lazy val free = project.settings(
  commonSettings : _*
).settings(
  autoScalaLibrary := false,
  libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3.3" % "test"
)

lazy val scalazBinding = project.settings(
  commonSettings : _*
).dependsOn(free)
