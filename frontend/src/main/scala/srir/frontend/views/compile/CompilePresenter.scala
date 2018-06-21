package srir.frontend.views.compile

import io.udash.Presenter
import io.udash.auth.AuthRequires
import io.udash.properties.model.ModelProperty
import srir.frontend.ApplicationContext
import srir.frontend.routing.CompileState
import srir.frontend.services.UserContextService

import scala.concurrent.ExecutionContext
import scala.util._

class CompilePresenter(
                     model: ModelProperty[CompileModel],
                     userService: UserContextService
                   )(
                    implicit ec: ExecutionContext
                   ) extends Presenter[CompileState.type] with AuthRequires {

  requireAuthenticated()(userService.getCurrentContext)

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
