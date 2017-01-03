enablePlugins(JavaAppPackaging)

name := """sample-akka-http-rest"""
organization := "samples"
version := "1.0"
scalaVersion := "2.12.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.16"
  val scalaTestV  = "3.0.1"
  val akkaHttpV = "10.0.1"
  val slickV = "3.2.0-M2"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttpV,
    "com.typesafe.slick" %% "slick" % slickV,
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "com.h2database" % "h2" % "1.4.193",
    "org.flywaydb" % "flyway-core" % "3.2.1",
    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

Revolver.settings
