package srir.frontend.views.compile

import io.udash.HasModelPropertyCreator
import org.scalajs.dom.File

case class CompileModel(compilerMessage: String, executionMessage: String, comparisonMessage: String, fileName: String, selectedFile: Seq[File])

object CompileModel extends HasModelPropertyCreator[CompileModel]