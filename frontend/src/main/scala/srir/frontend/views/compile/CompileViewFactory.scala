package srir.frontend.views.compile

import io.udash.{Application, ModelProperty, Presenter, View, ViewFactory}
import srir.frontend.routing.{CompileState, RoutingState}
import srir.frontend.services.{TranslationsService, UserContextService}
import srir.shared.model.compile.CompileInformation


/** Prepares model, view and presenter for demo view. */
class CompileViewFactory(
                            userService: UserContextService,
                            application: Application[RoutingState],
                            translationsService: TranslationsService
                          ) extends ViewFactory[CompileState.type] {

  import scala.concurrent.ExecutionContext.Implicits.global
  override def create(): (View, Presenter[CompileState.type]) = {
    // Main model of the view
    val model = ModelProperty[CompileModel](
      CompileModel(new CompileInformation("","",""),"")
    )


    val presenter = new CompilePresenter(model, userService)
    val view = new CompileView(model, presenter, translationsService)
    (view, presenter)
  }

}