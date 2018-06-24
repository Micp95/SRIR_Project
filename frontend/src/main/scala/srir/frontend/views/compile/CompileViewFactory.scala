package srir.frontend.views.compile


import io.udash.{Application, ModelProperty, Presenter, View, ViewFactory}
import srir.frontend.routing.{CompileState, RoutingState}


/** Prepares model, view and presenter for demo view. */
class CompileViewFactory(
                            application: Application[RoutingState]
                          ) extends ViewFactory[CompileState.type] {



  import scala.concurrent.ExecutionContext.Implicits.global
  override def create(): (View, Presenter[CompileState.type]) = {
    // Main model of the view
    val model = ModelProperty[CompileModel](
      CompileModel("","", Seq.empty, Seq.empty)
    )


    val presenter = new CompilePresenter(model )
    val view = new CompileView(model, presenter)
    (view, presenter)
  }

}