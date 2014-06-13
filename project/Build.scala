import sbt._
import Keys._
import play.Project._
import com.github.play2war.plugin._

object ApplicationBuild extends Build {

    val appName         = "resume"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      javaCore,
      javaJdbc,
      javaEbean,
      "uk.co.panaxiom" %% "play-jongo" % "0.6.0-jongo1.0",
      "com.typesafe" %% "play-plugins-redis" % "2.2.0"
  )

    val main = play.Project(appName, appVersion, appDependencies)
      .settings(Play2WarPlugin.play2WarSettings: _*)
      .settings(
        Play2WarKeys.servletVersion := "3.0",
        resolvers += "org.sedis" at "http://pk11-scratch.googlecode.com/svn/trunk"
    )

}
