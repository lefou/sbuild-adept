import de.tototec.sbuild._

@version("0.7.0")
@classpath("target/org.sbuild.plugins.adept-0.0.9000.jar")
class Test(implicit _project: Project) {

  import org.sbuild.plugins.adept._
  Plugin[Adept]

}
