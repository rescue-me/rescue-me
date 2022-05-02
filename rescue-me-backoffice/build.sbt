ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.6"

lazy val root = (project in file("."))
  .settings(
    name := "rescue-me-backoffice"
  )

/** ********* PROD DEPENDENCIES **************** */


libraryDependencies ++= Seq(
  "com.github.fd4s" %% "fs2-kafka" % "2.5.0-M3"
)
