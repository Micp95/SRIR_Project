package srir.backend.compile





import scala.reflect.runtime.{currentMirror, universe}
import scala.tools.reflect.ToolBox



case class CodeLoader (fileString:String){


  val fileContents: String = fileString
  val toolbox: ToolBox[universe.type] = currentMirror.mkToolBox()
  var tree: toolbox.u.Tree = toolbox.parse(s"import srir.backend.compile._;$fileContents")
  var compiledCode: () => Any = _

  try{
    compiledCode= toolbox.compile(tree)
  }catch {
    case ex: Exception =>
    case ex: Throwable =>
  }



  def getFileReference: ExternalProcess = compiledCode().asInstanceOf[ExternalProcess]
}
