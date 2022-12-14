ThisBuild / scalaVersion               := "3.2.0"
ThisBuild / version                    := "1.7.0"
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
  "org.mockito" % "mockito-core" % "4.9.0" % Test
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
    libraryDependencies ++= testLibraries,
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
      "io.javalin" % "javalin" % "5.2.0"
    ),
    libraryDependencies ++= testLibraries,
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
      "org.hibernate" % "hibernate-core" % "5.6.10.Final", // TODO: check 6.1.5.Final version
      "com.google.guava" % "guava" % "31.1-jre"
    ),
    libraryDependencies ++= testLibraries,
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishM2Configuration := publishM2Configuration.value.withOverwrite(true)
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
