package srir.frontend.routing

import srir.frontend.ApplicationContext
import srir.frontend.views.RootViewFactory
import srir.frontend.views.chat.ChatViewFactory
import srir.frontend.views.login.LoginPageViewFactory
import io.udash._
import srir.frontend.views.compile.CompileViewFactory

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] =
    state match {
      case RootState => new RootViewFactory(
        ApplicationContext.translationsService
      )
      case LoginPageState => new LoginPageViewFactory(
        ApplicationContext.userService, ApplicationContext.application, ApplicationContext.translationsService
      )
      case ChatState => new ChatViewFactory(
        ApplicationContext.userService, ApplicationContext.translationsService, ApplicationContext.notificationsCenter
      )
      case CompileState => new CompileViewFactory(
        ApplicationContext.userService, ApplicationContext.application, ApplicationContext.translationsService
      )
    }
}