package org.sbuild.plugins.adept

import java.io.File
import java.net.URLClassLoader
import de.tototec.sbuild.MavenSupport.MavenGav
import de.tototec.sbuild.Project
import de.tototec.sbuild.ResolveFiles
import de.tototec.sbuild.SchemeHandler.SchemeContext
import de.tototec.sbuild.SchemeResolver
import de.tototec.sbuild.TargetContext
import de.tototec.sbuild.TargetRefs
import de.tototec.sbuild.TargetRefs.fromString
import de.tototec.sbuild.Logger

object AdeptSchemeHandler {

  val version = InternalConstants.version

  trait Repository

}

class AdeptSchemeHandler(
  aetherClasspath: Seq[File] = Seq(),
  localRepoDir: File = new File(System.getProperty("user.home") + "/.m2/repository"),
  remoteRepos: Seq[AdeptSchemeHandler.Repository])(implicit project: Project)
    extends SchemeResolver {

  private[this] val log = Logger[AdeptSchemeHandler]

  def localPath(schemeCtx: SchemeContext): String = s"phony:${schemeCtx.fullName}"

  def resolve(schemeCtx: SchemeContext, targetContext: TargetContext) {
    val requestedDeps = schemeCtx.path.split(",").map(_.trim)
    log.debug("About to resolve the following requested dependencies: " + requestedDeps.mkString(", "))
  }

}

