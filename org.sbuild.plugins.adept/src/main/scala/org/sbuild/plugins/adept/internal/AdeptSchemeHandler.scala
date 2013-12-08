package org.sbuild.plugins.adept.internal

import de.tototec.sbuild.Logger
import de.tototec.sbuild.Project
import de.tototec.sbuild.SchemeHandler.SchemeContext
import de.tototec.sbuild.SchemeResolver
import de.tototec.sbuild.TargetContext
import adept.core.resolution.Resolver

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

    // TODO: create a appropriate context loader
    val variantsLoader = ???
    val resolver = new Resolver(variantsLoader)

    // TODO: assemble dependencies 
    val deps = ???

    resolver.resolve(deps) match {
      case Right(state) =>
        val ids = state.resolved
        ids.map { id => 
        id.value
        
        // TODO somehow unpack resolved result, download them and attach to context
        
        }
      case Left(state) => throw new RuntimeException("Could not resolve dependency: " + schemeCtx.fullName + "\nState: " + state)
    }
  }

}

