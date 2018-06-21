package srir.frontend.views

import io.udash.{StaticViewFactory, _}
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css.CssView
import srir.frontend.routing.RootState
import srir.shared.css.GlobalStyles

class RootViewFactory() extends StaticViewFactory[RootState.type](
  () => new RootView()
)

class RootView() extends ContainerView with CssView {
  import scalatags.JsDom.all._


  // ContainerView contains default implementation of child view rendering
  // It puts child view into `childViewContainer`
  override def getTemplate: Modifier = div(
    // loads Bootstrap and FontAwesome styles from CDN
    UdashBootstrap.loadBootstrapStyles(),
    UdashBootstrap.loadFontAwesome(),

    BootstrapStyles.container,
    div(
      GlobalStyles.floatRight
    ),
    h1("CompileProject"),
    childViewContainer
  )
}