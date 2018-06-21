package srir.shared.rest


import io.udash.rest.{Body, POST, REST}

import scala.concurrent.Future

@REST
trait CompilerServerREST {
  @POST def sendFile(@Body arg: String):Future[String]
  @POST def executeFile(@Body fileKey: String):Future[String]
}
