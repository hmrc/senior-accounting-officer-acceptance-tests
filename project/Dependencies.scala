import sbt.*

object Dependencies {

  val test: Seq[ModuleID] = Seq(
    "ch.qos.logback"       % "logback-classic" % "1.5.18" % Test,
    "com.vladsch.flexmark" % "flexmark-all"    % "0.64.8" % Test,
    "org.scalatest"       %% "scalatest"       % "3.2.19" % Test,
    "uk.gov.hmrc"         %% "ui-test-runner"  % "0.50.0" % Test,
    "uk.gov.hmrc"         %% "domain-play-30"  % "12.1.0" % Test,
    "com.github.javafaker" % "javafaker"       % "1.0.2"  % Test
  )
}
