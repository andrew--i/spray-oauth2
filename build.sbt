name := "oauth2"

scalaVersion := "2.11.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.4"

libraryDependencies += "com.typesafe.akka" %% "akka-http-core-experimental" % "0.5"

libraryDependencies += "com.typesafe.akka" %% "akka-stream-experimental" % "0.5"

libraryDependencies += "com.netaporter" %% "spray-json" % "1.2.7-NAP"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.13" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.3.4" % "test"

libraryDependencies += "io.spray" %% "spray-testkit" % "1.3.1" % "test"

libraryDependencies += "io.spray" %% "spray-httpx" % "1.3.1" % "test"

libraryDependencies += "io.spray" %% "spray-routing" % "1.3.1" % "test"