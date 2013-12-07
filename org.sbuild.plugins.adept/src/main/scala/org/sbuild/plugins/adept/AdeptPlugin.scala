package org.sbuild.plugins.adept

import org.sbuild.plugins.adept.internal.AdeptSchemeHandler

import de.tototec.sbuild.Plugin
import de.tototec.sbuild.Project
import de.tototec.sbuild.SchemeHandler

class Adept(val name: String)(implicit project: Project) {
  var schemeName: String = if(name == "") "adept" else name

}

class AdeptPlugin(implicit project: Project) extends Plugin[Adept] {

  def create(name: String): Adept = new Adept(name)

  override def applyToProject(instances: Seq[(String, Adept)]): Unit = instances foreach {
    case (name, adept) =>
      SchemeHandler(name, new AdeptSchemeHandler())
  }

}