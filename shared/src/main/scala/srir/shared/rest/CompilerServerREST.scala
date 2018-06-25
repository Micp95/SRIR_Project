package srir.shared.rest


import io.udash.rest.{Body, POST, REST}

import scala.concurrent.Future

@REST
trait CompilerServerREST {
  @POST def compileFile(@Body fileName: String):Future[String]
  @POST def executeFile(@Body fileName: String):Future[String]
  @POST def getStatsForFile(@Body fileName: String):Future[(String, Array[String],Array[Int])]

}
