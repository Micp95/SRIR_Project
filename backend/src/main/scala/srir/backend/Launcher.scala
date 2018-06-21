package srir.backend

import java.util.concurrent.TimeUnit

import io.udash.logging.CrossLogging
import srir.backend.server.ApplicationServer
import srir.backend.spring.SpringContext

import scala.io.StdIn

object Launcher extends CrossLogging {
  def main(args: Array[String]): Unit = {
    val startTime = System.nanoTime

    /*
    //test for file compiler
    new ExternalProcess {
      override def run() =10
    }
ApplicationServer
    val str =
      """new ExternalProcess {
         |  override def run() = 10
         |}""".stripMargin

    val xd =CodeLoader(str)
    val xddd = xd.getFileReference

    val res = xddd.run()

    println(res)
*/

    val ctx = SpringContext.createApplicationContext("beans.conf")
    val server = ctx.getBean(classOf[ApplicationServer])
    server.start()

    val duration: Long = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime - startTime)
    logger.info(s"Application started in ${duration}s.")

    // wait for user input and then stop the server
    logger.info("Click `Enter` to close application...")
    StdIn.readLine()
    server.stop()
  }
}