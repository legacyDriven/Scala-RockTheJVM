ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"
libraryDependencies += "org.scala-lang" %% "scala3-library" % "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "tutorial",
    idePackagePrefix := Some("com.eugeniusz.scala")
  )
