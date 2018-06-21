package srir.frontend.routing

import srir.frontend.ApplicationContext
import io.udash.{ViewFactory, ViewFactoryRegistry}
import srir.frontend.views.RootViewFactory
import srir.frontend.views.compile.CompileViewFactory

class StatesToViewFactoryDef extends ViewFactoryRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewFactory[_ <: RoutingState] =
    state match {
      case RootState => new RootViewFactory()
      case CompileState => new CompileViewFactory(
        ApplicationContext.application
      )
    }
}