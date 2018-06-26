package srir.backend.rest

import java.io.File
import java.nio.file.{Files, Paths}
import java.util.UUID

import io.udash.rest.Body
import org.json4s.DefaultFormats
import srir.shared.rest.ExecutionResponse
//import net.liftweb.json.DefaultFormats
import srir.backend.compile.CodeCompiler
import srir.backend.services.FilesStorage
import srir.backend.stats.FileManager
import srir.shared.UploadedFile
import srir.shared.rest.{CompileResponse, CompilerServerREST, MainServerREST}

import scala.concurrent.Future
import scala.util.Try

class ExposedRestInterfaces (filePath: String) extends MainServerREST{

  def mv(oldName: String, newName: String) =
    Try(new File(oldName).renameTo(new File(newName))).getOrElse(false)


  override def compileMethod(): CompilerServerREST = new CompilerServerREST{

//    import net.liftweb.json.Serialization.write
    implicit val formats = DefaultFormats
    import org.json4s.native.Serialization.write


    override def compileFile(@Body fileName: String):Future[String] ={

      val newFileName: String = s"${UUID.randomUUID()}_${fileName.replaceAll("[^a-zA-Z0-9.-]", "_")}"

      if(!Files.exists(Paths.get(s"$filePath/$fileName")) ||  !mv( s"$filePath/$fileName" ,  s"$filePath/$newFileName" )){
        Future.successful(write(CompileResponse(null,"Rename File failed")))
      }

      val compiler = new CodeCompiler
      val result = compiler.processFile(filePath + "/" + newFileName)

      if(result.isRight){
        FilesStorage.add(
          UploadedFile(newFileName, result.right.get)
        )
        Future.successful(write(CompileResponse(newFileName,null)))
      }else{
        Future.successful(write(CompileResponse(null,s"Compile failed: ${result.left.get.toString}")))
      }
    }

    override def executeFile(@Body fileName: String):Future[String]={
      val file = FilesStorage.getFile(fileName)

      try {

        val result = file.compiledApplication.run()
        Future.successful(write(ExecutionResponse(result,null)))

      }catch {
        case ex: Exception =>
          Future.successful(write(ExecutionResponse(0,s"Execution failed: ${ex.getMessage}")))
      }
    }

    override def getStatsForFile(@Body fileName: String): Future[(String, Array[String],Array[Int])] = {
      val manager=new FileManager

      manager.saveFile(filePath+"/"+fileName,fileName)
      val result=manager.compareFiles()

      Future.successful(result)
    }
  }

}
