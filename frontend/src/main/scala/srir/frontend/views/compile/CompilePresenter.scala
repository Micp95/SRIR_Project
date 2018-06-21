package srir.frontend.views.compile


import io.udash.Presenter
import io.udash.auth.AuthRequires
import io.udash.properties.model.ModelProperty
import srir.frontend.ApplicationContext
import srir.frontend.routing.CompileState

import scala.concurrent.ExecutionContext
import scala.util._

class CompilePresenter(
                     model: ModelProperty[CompileModel]
                   )(
                    implicit ec: ExecutionContext
                   ) extends Presenter[CompileState.type] with AuthRequires {

  def sendFile(): Unit ={


    ApplicationContext.restServer.compileMethod().sendFile("DUPA A NIE PLIK") onComplete{

      case Success(xd) =>

      case Failure(ex) =>

    }
  }

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
