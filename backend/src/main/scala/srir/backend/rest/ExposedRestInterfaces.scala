package srir.backend.rest

import io.udash.rest.Body
import srir.backend.compile.CodeCompiler
import srir.backend.services.FilesStorage
import srir.backend.stats.FileManager
import srir.shared.UploadedFile
import srir.shared.rest.{CompilerServerREST, MainServerREST}

import scala.concurrent.Future

class ExposedRestInterfaces (filePath: String) extends MainServerREST{

  override def compileMethod(): CompilerServerREST = new CompilerServerREST{


    override def compileFile(@Body fileName: String):Future[String] ={

      val compiler = new CodeCompiler
      val result = compiler.processFile(filePath + "/" + fileName)



      if(result.isRight){
        FilesStorage.add(
          UploadedFile(fileName, result.right.get)
        )
        Future.successful("OK")
      }else{
        Future.failed(new Exception("Compile failed:"+result.left.get.toString))
      }
    }

    override def executeFile(@Body fileName: String):Future[String]={
      val file = FilesStorage.getFile(fileName)

      try {
        val result = file.compiledApplication.run()

        val manager=new FileManager
        val counter=manager.countLines(filePath + "/" + fileName)
        manager.saveFile(filePath + "/" + fileName)

        Future.successful(result.toString + ":" + counter)
      }catch {
        case ex: Exception =>
          Future.failed(new Exception("Execution failed: "+ex.getMessage))
      }
    }

    override def getStatsForFile(@Body fileName: String): Future[Array[Int]] = {


      Future.successful(Array(0,0,0))
    }
  }

}
