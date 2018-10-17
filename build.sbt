
name := "oysterCard"

version := "0.1"

scalaVersion := "2.12.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val scalaTestV  = "3.0.1"
  Seq(
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}


parallelExecution in Test := false

Revolver.settings