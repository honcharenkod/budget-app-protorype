ThisBuild / scalaVersion := "3.10.1"

lazy val root = (project in file("."))
  .settings(
    name := "budget-app",
    scalacOptions ++= Seq("-deprecation", "-feature", "-Wunused:all")
  )

val zioVersion = "2.1.27"
val zioConfig = "4.1.2"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-json" % "1.1.2",
  "dev.zio" %% "zio-config" % zioConfig,
  "dev.zio" %% "zio-config-typesafe" % zioConfig,
  "dev.zio" %% "zio-config-magnolia" % zioConfig
)
