ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.6"

lazy val root = (project in file("."))
  .settings(
    name := "rescue-me-app"
  )

val Http4sVersion              = "0.21.28"
val CirceVersion               = "0.14.1"
val CirceGenericExVersion      = "0.14.1"
val CirceConfigVersion         = "0.8.0"
val CatsVersion                = "2.7.0"
val DoobieVersion              = "0.13.4"
val ScalaCheckVersion          = "1.15.4"
val ScalaTestVersion           = "3.2.9"
val ScalaTestPlusVersion       = "3.2.2.0"
val testcontainersScalaVersion = "0.40.5"

libraryDependencies ++= Seq(
  "com.typesafe"      % "config"                           % "1.4.2",
  "org.http4s"        %% "http4s-blaze-server"             % Http4sVersion,
  "org.http4s"        %% "http4s-circe"                    % Http4sVersion,
  "org.http4s"        %% "http4s-dsl"                      % Http4sVersion,
  "io.circe"          %% "circe-generic"                   % CirceVersion,
  "io.circe"          %% "circe-literal"                   % CirceVersion,
  "io.circe"          %% "circe-generic-extras"            % CirceGenericExVersion,
  "io.circe"          %% "circe-parser"                    % CirceVersion,
  "io.circe"          %% "circe-config"                    % CirceConfigVersion,
  "org.typelevel"     %% "cats-core"                       % CatsVersion,
  "org.tpolecat"      %% "doobie-core"                     % DoobieVersion,
  "org.tpolecat"      %% "doobie-postgres"                 % DoobieVersion,
  "org.tpolecat"      %% "doobie-scalatest"                % DoobieVersion,
  "org.tpolecat"      %% "doobie-hikari"                   % DoobieVersion,
  "org.http4s"        %% "http4s-blaze-client"             % Http4sVersion % Test,
  "org.scalacheck"    %% "scalacheck"                      % ScalaCheckVersion % Test,
  "org.scalatest"     %% "scalatest"                       % ScalaTestVersion % Test,
  "org.scalatestplus" %% "scalacheck-1-14"                 % ScalaTestPlusVersion % Test,
  "com.dimafeng"      %% "testcontainers-scala-scalatest"  % testcontainersScalaVersion % Test,
  "com.dimafeng"      %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % Test,
  "org.scalatestplus" %% "mockito-3-4"                     % "3.2.10.0" % Test
)

Test / fork := true
