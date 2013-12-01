package org.sbuild.plugins.adept

import java.io.File

import de.tototec.sbuild.MavenSupport.MavenGav

trait AdeptSchemeHandlerWorker {
  def resolve(requestedArtifacts: Seq[String]): Seq[File]
}

