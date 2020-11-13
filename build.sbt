import sbt.Keys.libraryDependencies

organization := "sk.nike"

name := "delta-concurrency-bug"

version := "1.0.0"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  //because we use SAM
  "-Xexperimental"
)
resolvers ++= Seq(
  //for local libs development
  Resolver.mavenLocal,
  "Nexus" at "http://nexus2.nike.sk/repository/nike-public/"
)


val sparkVersion = "3.0.1"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided exclude("com.fasterxml.jackson.core", "jackson-databind"),
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-streaming" % sparkVersion % Provided,
  "io.delta" %% "delta-core" % "0.7.0"
)
