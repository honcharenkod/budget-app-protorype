ThisBuild / scalaVersion := "3.10.1"

lazy val root = (project in file("."))
  .settings(
    name := "budget-app",
    scalacOptions ++= Seq("-deprecation", "-feature", "-Wunused:all")
  )
