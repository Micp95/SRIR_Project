package srir.backend.rest

import java.io.File
import java.util.UUID

import io.udash.rest.Body
import srir.backend.compile.CodeCompiler
import srir.backend.services.FilesStorage
import srir.backend.stats.FileManager
import srir.shared.UploadedFile
import srir.shared.rest.{CompilerServerREST, MainServerREST}

import scala.concurrent.Future
import scala.util.Try

class ExposedRestInterfaces (filePath: String) extends MainServerREST{

  override def compileMethod(): CompilerServerREST = new CompilerServerREST{


    private def mv(oldName: String, newName: String) =
      Try(new File(oldName).renameTo(new File(newName))).getOrElse(false)

    override def compileFile(@Body fileName: String):Future[String] ={

      val newFileName: String = s"${UUID.randomUUID()}_${fileName.replaceAll("[^a-zA-Z0-9.-]", "_")}"

      if(!mv(filePath + "/" + fileName,filePath + "/" + newFileName))
        Future.failed(new Exception("Rename File failed"))

      val compiler = new CodeCompiler
      val result = compiler.processFile(filePath + "/" + newFileName)

      if(result.isRight){
        FilesStorage.add(
          UploadedFile(newFileName, result.right.get)
        )
        Future.successful(newFileName)
      }else{
        Future.failed(new Exception("Compile failed:"+result.left.get.toString))
      }
    }

    override def executeFile(@Body fileName: String):Future[String]={
      val file = FilesStorage.getFile(fileName)

      try {

        val result = file.compiledApplication.run()
        Future.successful(result.toString)

      }catch {
        case ex: Exception =>
          Future.failed(new Exception("Execution failed: "+ex.getMessage))
      }
    }

    override def getStatsForFile(@Body fileName: String): Future[Array[Int]] = {
      val manager=new FileManager
      //val counter=manager.countLines(filePath + "/" + fileName)
      manager.saveFile(filePath + "/" + fileName)


      Future.successful(Array(0,0,0))
    }
  }

}
