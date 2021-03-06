package srir.backend.server

import io.udash.rest.server.{DefaultExposesREST, DefaultRestServlet}
import javax.servlet.MultipartConfigElement
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.servlet.{DefaultServlet, ServletContextHandler, ServletHolder}
import srir.backend.rest.{DemoFileUploadServlet, ExposedRestInterfaces}
import srir.shared.ApplicationServerContexts
import srir.shared.rest.MainServerREST

import scala.concurrent.ExecutionContext.Implicits.global

class ApplicationServer(val port: Int, resourceBase: String) {
  private val server = new Server(port)
  private val contextHandler = new ServletContextHandler
  private val appHolder = createAppHolder()

  contextHandler.setSessionHandler(new SessionHandler)
  contextHandler.getSessionHandler.addEventListener(new org.atmosphere.cpr.SessionSupport())
  contextHandler.addServlet(appHolder, "/*")
  server.setHandler(contextHandler)

  def start(): Unit = server.start()
  def stop(): Unit = server.stop()

  private val restHolder = new ServletHolder(

   new DefaultRestServlet(new DefaultExposesREST[MainServerREST](new ExposedRestInterfaces(resourceBase + "/uploads")))

  )
  restHolder.setAsyncSupported(true)

  contextHandler.addServlet(restHolder, "/api/*")

  private val uploadsHolder = {
    val holder = new ServletHolder(new DemoFileUploadServlet(resourceBase + "/uploads"))
    holder.getRegistration.setMultipartConfig(new MultipartConfigElement(""))
    holder
  }

  contextHandler.addServlet(uploadsHolder, ApplicationServerContexts.uploadContextPrefix + "/*")

  private def createAppHolder() = {
    val appHolder = new ServletHolder(new DefaultServlet)
    appHolder.setAsyncSupported(true)
    appHolder.setInitParameter("resourceBase", resourceBase)
    appHolder
  }
}
