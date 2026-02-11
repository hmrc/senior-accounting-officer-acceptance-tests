lazy val root = (project in file("."))
  .settings(
    name := "senior-accounting-officer-acceptance-tests",
    version := "0.1.0",
    scalaVersion := "3.3.4",
    libraryDependencies ++= Dependencies.test
  )
