package org.sbuild.plugins.adept

import org.sbuild.plugins.adept.internal.AdeptSchemeHandler
import de.tototec.sbuild.Plugin
import de.tototec.sbuild.Project
import de.tototec.sbuild.SchemeHandler
import de.tototec.sbuild.Target
import de.tototec.sbuild.Prop
import de.tototec.sbuild.TargetRef

class Adept(val name: String)(implicit project: Project) {
  var schemeName: String = if (name == "") "adept" else name
  // TODO: maintain some kind of alias map, such that we can refer to a preconfigured collection of dependencies by the alias. Think of as "compile" as an alias to all compile dependencies.
  var aliases: Seq[(String, Seq[String])] = Seq()
}

class AdeptPlugin(implicit project: Project) extends Plugin[Adept] {

  // TODO: configure adept
  def create(name: String): Adept = new Adept(name)

  override def applyToProject(instances: Seq[(String, Adept)]): Unit = instances foreach {
    case (name, adept) =>
      SchemeHandler(adept.schemeName, new AdeptSchemeHandler())
      val listDeps = Seq(Prop("adept.deps", "")).collect { case x if x.size > 0 => TargetRef(s"${adept.schemeName}:${x}") }
      Target("adept-dependencies") dependsOn listDeps exec {
        val given = Prop("adept.deps")
        println("Given: " + given)
        println("Resolved: " + listDeps.files)
      } help "Resolve dependencies via adept. The request must be given as property 'adept.deps'."
  }

}