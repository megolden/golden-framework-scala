ThisBuild / scalaVersion               := "3.2.0"
ThisBuild / version                    := "1.10.0"
ThisBuild / organization               := "io.github.megolden"
ThisBuild / organizationName           := "golden"
ThisBuild / organizationHomepage       := Some(url("https://github.com/megolden"))
ThisBuild / licenses                   := List("MIT" -> url("https://opensource.org/licenses/MIT"))
ThisBuild / homepage                   := Some(url("https://github.com/megolden/golden-framework-scala"))
ThisBuild / crossPaths                 := false
ThisBuild / versionScheme              := Some("early-semver")
ThisBuild / scmInfo                    := Some(
  ScmInfo(
    url("https://github.com/megolden/golden-framework-scala"),
    "scm:git@github.com:megolden/golden-framework-scala.git"
  )
)
ThisBuild / developers                 := List(
  Developer(
    id    = "megolden",
    name  = "Mehdi Eftekhari",
    email = "me.golden2008@gmail.com",
    url   = url("https://github.com/megolden")
  )
)
ThisBuild / pomIncludeRepository       := { _ => false }
ThisBuild / publishTo                  := {
  val nexus = "https://s01.oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle          := true
ThisBuild / exportJars                 := true
ThisBuild / credentials                += Credentials(Path.userHome / ".sbt" / ".credentials")

val testLibraries = Seq(
  "org.scalatest" %% "scalatest" % "3.2.14" % Test,
  "org.mockito" % "mockito-core" % "4.10.0" % Test
)

lazy val core = project
  .in(file("src/core"))
  .settings(
    name := "framework-core",
    description := "core libraries",
    libraryDependencies ++= Seq(
      "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.1",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % "2.14.1",
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.14.1",
      "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.14.1",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.14.1",
      "com.ibm.icu" % "icu4j" % "72.1"
    ),
    libraryDependencies ++= testLibraries,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
  )

lazy val bind = project
  .in(file("src/bind"))
  .dependsOn(core)
  .settings(
    name := "framework-bind",
    description := "a lightweight dependency injection library",
    libraryDependencies ++= Seq(
      "javax.inject" % "javax.inject" % "1"
    ),
    libraryDependencies ++= testLibraries ++ Seq(
      "com.google.inject" % "guice" % "5.1.0" % Test
    ),
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
  )

lazy val validation = project
  .in(file("src/validation"))
  .dependsOn(core)
  .settings(
    name := "framework-validation",
    description := "a validation library",
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.12.0"
    ),
    libraryDependencies ++= testLibraries,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
  )

lazy val web = project
  .in(file("src/web"))
  .dependsOn(core, bind)
  .settings(
    name := "framework-web",
    description := "a simple web framework - uses Javalin framework under the hood",
    libraryDependencies ++= Seq(
      "io.javalin" % "javalin" % "5.2.0",
      "org.slf4j" % "slf4j-simple" % "2.0.6"
    ),
    libraryDependencies ++= testLibraries ++ Seq(
      "org.scalaj" % "scalaj-http_2.13" % "2.4.2" % Test
    ),
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
  )

lazy val hibernate = project
  .in(file("src/hibernate"))
  .dependsOn(core)
  .settings(
    name := "framework-hibernate",
    description := "a utility library for hibernate",
    libraryDependencies ++= Seq(
      "org.hibernate.orm" % "hibernate-core" % "6.1.6.Final",
      "org.hibernate.orm" % "hibernate-ant" % "6.1.6.Final",
      "com.google.guava" % "guava" % "31.1-jre"
    ),
    libraryDependencies ++= testLibraries ++ Seq(
      "org.hsqldb" % "hsqldb" % "2.7.1" % Test,
      "com.h2database" % "h2" % "2.1.214" % Test,
      "org.mariadb.jdbc" % "mariadb-java-client" % "3.1.0" % Test,
      "org.dom4j" % "dom4j" % "2.1.4" % Test
    ),
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true),
    publish / skip := true
  )

lazy val root = project
  .in(file("."))
  .dependsOn(core, bind, validation, web, hibernate)
  .aggregate(core, bind, validation, web, hibernate)
  .settings(
    name := "framework",
    description := "a simple framework for Scala",
    publish / skip := true
  )
