scalaVersion := "2.11.1"

scalacOptions ++= Seq("-Xexperimental", "-deprecation")

javacOptions += "-Xlint:unchecked"

autoScalaLibrary := false

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3.3" % "test"

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))
