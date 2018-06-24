package srir.backend.compile

import srir.shared.model.compile.ExternalProcess
import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox

case class CodeLoader (fileString:String){


  private val fileContents: String = fileString
  private val toolbox: ToolBox[universe.type] = currentMirror.mkToolBox()
  private val tree: toolbox.u.Tree = toolbox.parse(s"import srir.backend.compile._;import srir.shared.model.compile._;  $fileContents")
  var compiledCode: () => Any = _

  try{
    compiledCode= toolbox.compile(tree)
  }catch {
    case ex: Exception =>
    case ex: Throwable =>
  }



  def getFileReference: ExternalProcess = compiledCode().asInstanceOf[ExternalProcess]
}
