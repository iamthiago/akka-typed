name := "images-typed"

version := "0.1"

scalaVersion := "2.12.5"

val akkaV = "2.5.8"
val akkaHttpV = "10.0.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaV % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % Test,
  "com.typesafe.akka" %% "akka-typed" % akkaV,
  "com.typesafe.akka" %% "akka-typed-testkit" % akkaV % Test
)
