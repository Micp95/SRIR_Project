package srir.backend.compile


import scala.reflect.runtime.currentMirror
import scala.tools.reflect.ToolBox


case class CodeLoader (fileString:String){


  val fileContents = fileString
  val toolbox = currentMirror.mkToolBox()
  val tree = toolbox.parse(s"import srir.backend.compile._;$fileContents")
  val compiledCode = toolbox.compile(tree)

  def getFileReference: ExternalProcess = compiledCode().asInstanceOf[ExternalProcess]
}
