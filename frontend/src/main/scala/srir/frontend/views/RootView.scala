package srir.frontend.views

import io.udash.bootstrap.utils.UdashJumbotron
import io.udash.{StaticViewFactory, _}
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.css.CssView
import srir.frontend.routing.RootState
//import srir.shared.css.CompileStyles
import scalatags.JsDom.tags2.main

class RootViewFactory() extends StaticViewFactory[RootState.type](
  () => new RootView()
)

class RootView() extends ContainerView with CssView {
  import scalatags.JsDom.all._

  // ContainerView contains default implementation of child view rendering
  // It puts child view into `childViewContainer`
  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    UdashBootstrap.loadFontAwesome(),

    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("Compile Project"),
          p("Welcome in the Compile project!")
        ).render,
        childViewContainer
      )
    )
  ).render

  override def getTemplate: Modifier = content
}