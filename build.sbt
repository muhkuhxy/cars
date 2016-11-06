name := "cars"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.1" % "test",
  "org.scalatestplus" %% "play" % "1.4.0" % "test",
  jdbc,
  "com.typesafe.play" %% "anorm" % "2.4.0",
  evolutions

)