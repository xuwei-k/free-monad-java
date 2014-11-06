val commonSettings = Seq(
  scalaVersion := "2.11.4",
  scalacOptions ++= Seq("-Xexperimental", "-deprecation"),
  javacOptions += "-Xlint:unchecked",
  licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))
)


lazy val root = project.in(file(".")).settings(
  commonSettings: _*
).aggregate(free, scalazBinding)

lazy val free = project.settings(
  commonSettings : _*
).settings(
  autoScalaLibrary := false,
  libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.3.3" % "test"
)

val scalazV = "7.1.0"

def specLiteURL = s"https://raw.github.com/scalaz/scalaz/v${scalazV}/tests/src/test/scala/scalaz/SpecLite.scala"
def specLite = SettingKey[List[String]]("specLite")
def specLiteFile(dir: File, contents: List[String]): File = {
  val file = dir / "SpecLite.scala"
  IO.writeLines(file, contents)
  file
}

lazy val scalazBinding = project.settings(
  commonSettings : _*
).settings(
  libraryDependencies += "org.scalaz" %% "scalaz-scalacheck-binding" % scalazV % "test",
  libraryDependencies += "org.scalaz" %% "scalaz-core" % scalazV,
  specLite := {
    println(s"downloading from ${specLiteURL}")
    val lines = IO.readLinesURL(url(specLiteURL))
    println("download finished")
    lines
  },
  sourceGenerators in Test += task{
    Seq(specLiteFile((sourceManaged in Test).value, specLite.value))
  }
).dependsOn(free)
