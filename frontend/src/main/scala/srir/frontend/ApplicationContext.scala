package srir.frontend

import srir.frontend.routing.{CompileState, RoutingRegistryDef, RoutingState, StatesToViewFactoryDef}
import io.udash._
import org.scalajs.dom
import srir.shared.rest.MainServerREST
import io.udash.rest._
import scala.util.Try

object ApplicationContext {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val routingRegistry = new RoutingRegistryDef
  private val viewFactoryRegistry = new StatesToViewFactoryDef

  val application = new Application[RoutingState](
    routingRegistry, viewFactoryRegistry
  )

  application.onRoutingFailure {
    case _: Exception =>
      // automatic redirection to Main Page
      application.goTo(CompileState)
  }

  val restServer: MainServerREST = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  )

}