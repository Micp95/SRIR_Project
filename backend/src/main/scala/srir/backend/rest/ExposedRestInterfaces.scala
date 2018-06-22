package srir.backend.rest

import io.udash.rest.Body
import srir.backend.compile.CodeCompiler
import srir.shared.rest.{CompilerServerREST, MainServerREST}

import scala.concurrent.Future

class ExposedRestInterfaces (filePath: String) extends MainServerREST{

  override def compileMethod(): CompilerServerREST = new CompilerServerREST{


    override def sendFile(@Body arg: String):Future[String] ={

      val fileName = arg

      val compiler = new CodeCompiler
      val result = compiler.processFile(filePath + "/" + fileName)


      if(result.isRight){
        //result.right.get.run()

        Future.successful("XDDDDD")
      }else{
        Future.failed(new Exception("Compile failed"))
      }
    }

    override def executeFile(@Body fileKey: String):Future[String]=
      Future.successful("XD")

  }

}
