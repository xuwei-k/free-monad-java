scalaVersion := "2.11.1"

scalacOptions ++= Seq("-Xexperimental", "-deprecation")

javacOptions += "-Xlint:unchecked"

autoScalaLibrary := false

licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))
