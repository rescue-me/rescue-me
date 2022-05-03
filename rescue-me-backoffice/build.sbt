ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.6"

lazy val root = (project in file("."))
  .settings(
    name := "rescue-me-backoffice"
  )

/** ********* PROD DEPENDENCIES **************** */

resolvers += "confluent" at "https://packages.confluent.io/maven/"

libraryDependencies ++= Seq(
  "com.github.fd4s" %% "fs2-kafka" % "2.5.0-M3",
  "com.github.fd4s" %% "fs2-kafka-vulcan" % "2.5.0-M3",

  "org.apache.kafka" % "kafka-clients" % "2.8.0",
  "org.apache.kafka" % "kafka-streams" % "2.8.0",
  "org.apache.kafka" %% "kafka-streams-scala" % "2.8.0",
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"
)
