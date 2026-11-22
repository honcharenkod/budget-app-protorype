ThisBuild / scalaVersion := "3.10.1"

lazy val root = (project in file("."))
  .settings(
    name := "budget-app",
    scalacOptions ++= Seq("-deprecation", "-feature", "-Wunused:all")
  )

val zioVersion = "2.1.27"
val zioConfig = "4.1.2"
val zioLogging = "2.5.5"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-json" % "1.0.0", // must match what zio-http's zio-schema-json was built against
  "dev.zio" %% "zio-config" % zioConfig,
  "dev.zio" %% "zio-config-typesafe" % zioConfig,
  "dev.zio" %% "zio-config-magnolia" % zioConfig,
  "dev.zio" %% "zio-http" % "3.11.6",

  // Quill pulls zio-json 0.10.x only for JsonValue/JsonbValue; we use our own jsonb codec instead.
  ("dev.zio" %% "quill-jdbc-zio" % "4.9.0").exclude("dev.zio", "zio-json_3"),
  "org.postgresql" % "postgresql" % "42.7.14",

  "org.slf4j" % "slf4j-api" % "2.0.20",
  "dev.zio" %% "zio-logging" % zioLogging,
  "dev.zio" %% "zio-logging-slf4j2-bridge" % zioLogging
)
