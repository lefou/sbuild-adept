import de.tototec.sbuild._
import de.tototec.sbuild.ant._
import de.tototec.sbuild.ant.tasks._
import de.tototec.sbuild.TargetRefs._

@version("0.6.0")
@classpath("mvn:org.apache.ant:ant:1.8.4")
class SBuild(implicit _project: Project) {

  val namespace = "org.sbuild.plugins.adept"
  val version = "0.0.9000"

  val sbuildVersion = "0.6.0.9004"
  val scalaVersion = "2.10.3"
  val scalaBinVersion = "2.10"
  val sprayRepoUrl = "http://repo.spray.io/spray"
  val json4sVersion = "3.2.5"
  val akkaVersion = "2.1.4"
  val sprayVersion = "1.2-M8"

  val jar = s"target/${namespace}-${version}.jar"
  val sourcesZip = s"target/${namespace}-${version}-sources.jar"

  val scalaCompiler = s"mvn:org.scala-lang:scala-compiler:$scalaVersion" ~
    s"mvn:org.scala-lang:scala-library:$scalaVersion" ~
    s"mvn:org.scala-lang:scala-reflect:$scalaVersion"

  // FIXME after SBuild 0.7.0 is released
  val sbuildCore = s"../../sbuild/de.tototec.sbuild/target/de.tototec.sbuild-${sbuildVersion}.jar"

  SchemeHandler("akka", new MvnSchemeHandler(repos = Seq("http://repo.typesafe.com/typesafe/releases")))

  SchemeHandler("spray", new MvnSchemeHandler(repos = Seq(sprayRepoUrl)))

  val adeptCp =
    s"mvn:org.scala-lang:scala-library:${scalaVersion}" ~
      "mvn:org.apache.ivy:ivy:2.3.0-rc1" ~
      "mvn:org.eclipse.jgit:org.eclipse.jgit:2.3.1.201302201838-r" ~
      "mvn:com.jcraft:jsch:0.1.50" ~
      "mvn:org.slf4j:slf4j-api:1.7.5" ~
      "mvn:ch.qos.logback:logback-core:1.0.9" ~
      "mvn:ch.qos.logback:logback-classic:1.0.9" ~
      "mvn:com.typesafe:config:1.0.2" ~
      s"mvn:org.json4s:json4s-native_${scalaBinVersion}:${json4sVersion}" ~
      s"mvn:org.json4s:json4s-core_${scalaBinVersion}:${json4sVersion}" ~
      s"mvn:org.json4s:json4s-ast_${scalaBinVersion}:${json4sVersion}" ~
      s"mvn:com.typesafe.akka:akka-actor_${scalaBinVersion}:2.1.4" ~
      s"spray:io.spray:spray-http:${sprayVersion}" ~
      s"spray:io.spray:spray-util:${sprayVersion}" ~
      s"spray:io.spray:spray-can:${sprayVersion}"

  val compileCp =
    s"mvn:org.scala-lang:scala-library:${scalaVersion}" ~
      sbuildCore ~
      adeptCp

  ExportDependencies("eclipse.classpath", compileCp)

  Target("phony:all") dependsOn sourcesZip ~ jar

  Target("phony:clean").evictCache exec {
    AntDelete(dir = Path("target"))
  }

  val versionScala = Target("target/generated-scala/InternalConstants.scala") dependsOn _project.projectFile exec { ctx: TargetContext =>
    AntMkdir(dir = ctx.targetFile.get.getParentFile)
    AntEcho(file = ctx.targetFile.get, message = s"""|// GENERATED
      |package ${namespace}
      |
      |/** Generated file. */
      |private object InternalConstants {
      |  /** The SBuild version this class was built with. */
      |  def version = "${version}"
      |}
      |""".stripMargin
    )
  }

  val sources = "scan:src/main/scala" ~ versionScala

  Target("phony:compile").cacheable dependsOn scalaCompiler ~ compileCp ~ sources exec {
    val output = "target/classes"
    addons.scala.Scalac(
      compilerClasspath = scalaCompiler.files,
      classpath = compileCp.files,
      sources = sources.files,
      destDir = Path(output),
      unchecked = true, deprecation = true, debugInfo = "vars"
    )
  }

  Target(sourcesZip) dependsOn sources ~ "LICENSE.txt" exec { ctx: TargetContext =>
    AntZip(destFile = ctx.targetFile.get, fileSets = Seq(
      AntFileSet(dir = Path("src/main/scala")),
      AntFileSet(dir = Path("target/generated-scala")),
      AntFileSet(file = Path("LICENSE.txt"))
    ))
  }

  Target("phony:scaladoc").cacheable dependsOn scalaCompiler ~ compileCp ~ sources exec {
    addons.scala.Scaladoc(
      scaladocClasspath = scalaCompiler.files,
      classpath = compileCp.files,
      sources = sources.files,
      destDir = Path("target/scaladoc"),
      deprecation = true, unchecked = true, implicits = true,
      docVersion = sbuildVersion,
      docTitle = s"SBuild Experimental API Reference"
    )
  }

  Target(jar) dependsOn "compile" ~ "LICENSE.txt" exec { ctx: TargetContext =>
    AntJar(
      destFile = ctx.targetFile.get,
      baseDir = Path("target/classes"),
      fileSet = AntFileSet(file = Path("LICENSE.txt")),
      manifestEntries = Map(
        "SBuild-ExportPackage" -> namespace,
        "SBuild-Plugin" -> s"""${namespace}.Adept=${namespace}.AdeptPlugin;version="${version}"""",
        "SBuild-Classpath" -> Seq(
          "mvn:org.apache.ivy:ivy:2.3.0-rc1",
          "mvn:org.eclipse.jgit:org.eclipse.jgit:2.3.1.201302201838-r",
          "mvn:com.jcraft:jsch:0.1.50",
          "mvn:org.slf4j:slf4j-api:1.7.5",
          "mvn:ch.qos.logback:logback-core:1.0.9",
          "mvn:ch.qos.logback:logback-classic:1.0.9",
          "mvn:com.typesafe:config:1.0.2",
          s"mvn:org.json4s:json4s-native_${scalaBinVersion}:${json4sVersion}",
          s"mvn:org.json4s:json4s-core_${scalaBinVersion}:${json4sVersion}",
          s"mvn:org.json4s:json4s-ast_${scalaBinVersion}:${json4sVersion}",
          s"mvn:com.typesafe.akka:akka-actor_${scalaBinVersion}:akkaVersion",
          s"${sprayRepoUrl}/io/spray/spray-http/${sprayVersion}/spray-http-${sprayVersion}.jar",
          s"${sprayRepoUrl}/io/spray/spray-util/${sprayVersion}/spray-util-${sprayVersion}.jar",
          s"${sprayRepoUrl}/io(spray/spray-can/${sprayVersion}/spray-can-${sprayVersion}.jar"
        ).map("raw:" + _).mkString(",")
      )
    )
  }

}
