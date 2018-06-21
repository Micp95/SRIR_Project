package srir.frontend

import srir.frontend.routing.{LoginPageState, RoutingRegistryDef, RoutingState, StatesToViewFactoryDef}
import srir.frontend.services.rpc.{NotificationsCenter, RPCService}
import srir.frontend.services.{TranslationsService, UserContextService}
import srir.shared.model.SharedExceptions
import srir.shared.rpc.client.MainClientRPC
import srir.shared.rpc.server.MainServerRPC
import io.udash._
import io.udash.rpc._
import org.scalajs.dom
import srir.shared.rest.MainServerREST

import scala.util.Try

object ApplicationContext {
  import scala.concurrent.ExecutionContext.Implicits.global

  private val routingRegistry = new RoutingRegistryDef
  private val viewFactoryRegistry = new StatesToViewFactoryDef

  val application = new Application[RoutingState](
    routingRegistry, viewFactoryRegistry
  )

  application.onRoutingFailure {
    case _: SharedExceptions.UnauthorizedException =>
      // automatic redirection to LoginPage
      application.goTo(LoginPageState)
  }

  val notificationsCenter: NotificationsCenter = new NotificationsCenter

  // creates RPC connection to the server
  val serverRpc: MainServerRPC = DefaultServerRPC[MainClientRPC, MainServerRPC](
    new RPCService(notificationsCenter), exceptionsRegistry = new SharedExceptions
  )

  val translationsService: TranslationsService = new TranslationsService(serverRpc.translations())
  val userService: UserContextService = new UserContextService(serverRpc)

  import io.udash.rest._
  val restServer: MainServerREST = DefaultServerREST[MainServerREST](
    Protocol.Http, dom.window.location.hostname, Try(dom.window.location.port.toInt).getOrElse(80), "/api/"
  )



}