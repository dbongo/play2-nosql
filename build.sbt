name := "play2-nosql"

version := "1.0-SNAPSHOT"

resolvers += "Typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache
)     

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.2.3")

play.Project.playJavaSettings
