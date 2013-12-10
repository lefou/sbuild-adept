package org.sbuild.plugins.adept.internal

import de.tototec.sbuild.Logger
import de.tototec.sbuild.Project
import de.tototec.sbuild.SchemeHandler.SchemeContext
import de.tototec.sbuild.SchemeResolver
import de.tototec.sbuild.TargetContext
import adept.core.resolution.Resolver
import adept.core.models.ResolveResult
import adept.core.models.ResolvedResult
import adept.core.models.UnderconstrainedResult
import adept.core.models.OverconstrainedResult

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
      case r: ResolvedResult =>
      //        val ids = state
      //        ids.map { id =>
      //          id.value

      // TODO somehow unpack resolved result, download them and attach to context

      //        }

      case u: UnderconstrainedResult => throw new RuntimeException("Could not resolve underconstrained dependency: " + schemeCtx.fullName + "\nResult: " + u)
      case o: OverconstrainedResult => throw new RuntimeException("Could not resolve overconstrained dependency: " + schemeCtx.fullName + "\nResult: " + o)
    }
  }

}

