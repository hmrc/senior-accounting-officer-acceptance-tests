lazy val root = (project in file("."))
  .settings(
    name         := "senior-accounting-officer-acceptance-tests",
    version      := "0.1.0",
    scalaVersion := "3.3.4",
    libraryDependencies ++= Dependencies.test
  )
  .settings(scalafixSettings *)

val scalafixSettings: Seq[Setting[?]] = Seq(
  semanticdbEnabled := true // enable SemanticDB
)

addCommandAlias("checkLint", "scalafmtSbtCheck;scalafmtCheckAll")
addCommandAlias("lint", "scalafixAll;scalafmtSbt;scalafmtAll")
