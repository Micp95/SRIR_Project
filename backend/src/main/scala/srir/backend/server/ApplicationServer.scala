package srir.backend.server

import io.udash.rest.server.{DefaultExposesREST, DefaultRestServlet}
import srir.backend.services.DomainServices
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}
import srir.backend.rest.ExposedRestInterfaces
import srir.shared.rest.MainServerREST

import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationServer(val port: Int, resourceBase: String, domainServices: DomainServices) {
  private val server = new Server(1234)
  private val contextHandler = new ServletContextHandler
  private val appHolder = createAppHolder()

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.getSessionHandler.addEventListener(new org.atmosphere.cpr.SessionSupport())
  contextHandler.addServlet(appHolder, "/*")
  server.setHandler(contextHandler)


  private val restHolder = new ServletHolder(
    new DefaultRestServlet(new DefaultExposesREST[MainServerREST](new ExposedRestInterfaces)))
  restHolder.setAsyncSupported(true)

  contextHandler.addServlet(restHolder, "/api/*")



  def start(): Unit = server.start()
  def stop(): Unit = server.stop()



  private def createAppHolder() = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }
}