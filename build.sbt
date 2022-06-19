ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.6"

val Http4sVersion              = "0.23.11"
val CirceVersion               = "0.14.1"
val CirceGenericExVersion      = "0.14.1"
val CirceConfigVersion         = "0.8.0"
val CatsVersion                = "2.7.0"
val DoobieVersion              = "1.0.0-RC1"
val ScalaCheckVersion          = "1.16.0"
val ScalaTestVersion           = "3.2.12"
val ScalaTestPlusVersion       = "3.2.2.0"
val testcontainersScalaVersion = "0.40.7"
val log4catsVersion            = "2.3.1"

lazy val app = (project in file("app"))
  .settings(
    name := "app",
    libraryDependencies ++= Seq(
      "com.typesafe"       % "config"                          % "1.4.2",
      "org.http4s"        %% "http4s-ember-server"             % Http4sVersion,
      "org.http4s"        %% "http4s-circe"                    % Http4sVersion,
      "org.http4s"        %% "http4s-dsl"                      % Http4sVersion,
      "io.circe"          %% "circe-generic"                   % CirceVersion,
      "io.circe"          %% "circe-literal"                   % CirceVersion,
      "io.circe"          %% "circe-generic-extras"            % CirceGenericExVersion,
      "io.circe"          %% "circe-parser"                    % CirceVersion,
      "io.circe"          %% "circe-config"                    % CirceConfigVersion,
      "org.typelevel"     %% "cats-core"                       % CatsVersion,
      "org.typelevel"     %% "log4cats-slf4j"                  % log4catsVersion,
      "org.tpolecat"      %% "doobie-core"                     % DoobieVersion,
      "org.tpolecat"      %% "doobie-postgres"                 % DoobieVersion,
      "org.tpolecat"      %% "doobie-scalatest"                % DoobieVersion,
      "org.tpolecat"      %% "doobie-hikari"                   % DoobieVersion,
      "org.http4s"        %% "http4s-blaze-client"             % Http4sVersion              % Test,
      "org.scalacheck"    %% "scalacheck"                      % ScalaCheckVersion          % Test,
      "org.scalatest"     %% "scalatest"                       % ScalaTestVersion           % Test,
      "org.scalatestplus" %% "scalacheck-1-14"                 % ScalaTestPlusVersion       % Test,
      "com.dimafeng"      %% "testcontainers-scala-scalatest"  % testcontainersScalaVersion % Test,
      "com.dimafeng"      %% "testcontainers-scala-postgresql" % testcontainersScalaVersion % Test,
      "org.scalatestplus" %% "mockito-3-4"                     % "3.2.10.0"                 % Test,
      "com.github.alexarchambault" %% "scalacheck-shapeless_1.15" % "1.3.0"
    )
  )

lazy val backoffice = (project in file("backoffice"))
  .settings(
    name := "backoffice",
    resolvers += "confluent" at "https://packages.confluent.io/maven/",
    libraryDependencies ++= Seq(
      "com.github.fd4s"  %% "fs2-kafka"           % "2.5.0-M3",
      "com.github.fd4s"  %% "fs2-kafka-vulcan"    % "2.5.0-M3",
      "org.apache.kafka"  % "kafka-clients"       % "3.1.0",
      "org.apache.kafka"  % "kafka-streams"       % "3.1.0",
      "org.apache.kafka" %% "kafka-streams-scala" % "3.1.0",
      "io.circe"         %% "circe-core"          % "0.14.1",
      "io.circe"         %% "circe-generic"       % "0.14.1",
      "io.circe"         %% "circe-parser"        % "0.14.1"
    )
  )

Test / fork := true
