package srir.backend.compile

import java.io.File

import srir.shared.rest.CompileStatus._

import scala.io.Source

class CodeCompiler{
  def processFile (filePath:String) : Either[CompileStatus, ExternalProcess] = {

    if (!new File(filePath).exists())
      return Left(WrongPath)


    try {

      val fileContents = Source.fromFile(filePath).getLines().mkString("\n")

      if(!(fileContents contains "ExternalProcess"))
        return Left(MissExternalProcess)

      if(!(fileContents contains "run()"))
        return Left(MissRun)

      val compiledFile = CodeLoader(fileContents)
      val result = compiledFile.getFileReference

      Right(result)
    }catch {
      case ex: Exception => Left(FailedCompilation)
    }

  }
}
