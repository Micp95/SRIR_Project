package srir.backend.rest

import io.udash.rest.Body
import srir.shared.rest.{CompilerServerREST, MainServerREST}

import scala.concurrent.Future

class ExposedRestInterfaces extends MainServerREST{

  override def compileMethod(): CompilerServerREST = new CompilerServerREST{


    override def sendFile(@Body arg: String):Future[String] =
      Future.successful("XDDDDD")

    override def executeFile(@Body fileKey: String):Future[String]=
      Future.successful("XD")

  }

}
