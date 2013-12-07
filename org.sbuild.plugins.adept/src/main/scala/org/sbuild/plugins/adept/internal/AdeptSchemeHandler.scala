package org.sbuild.plugins.adept.internal

import de.tototec.sbuild.Logger
import de.tototec.sbuild.Project
import de.tototec.sbuild.SchemeHandler.SchemeContext
import de.tototec.sbuild.SchemeResolver
import de.tototec.sbuild.TargetContext

object AdeptSchemeHandler {
  trait Repository
}

class AdeptSchemeHandler()(implicit project: Project)
    extends SchemeResolver {

  private[this] val log = Logger[AdeptSchemeHandler]

  def localPath(schemeCtx: SchemeContext): String = s"phony:${schemeCtx.fullName}"

  def resolve(schemeCtx: SchemeContext, targetContext: TargetContext) {
    val requestedDeps = schemeCtx.path.split(",").map(_.trim)
    log.debug("About to resolve the following requested dependencies: " + requestedDeps.mkString(", "))
  }

}

