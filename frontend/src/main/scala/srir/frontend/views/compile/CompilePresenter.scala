package srir.frontend.views.compile

import io.udash._
import io.udash.auth.AuthRequires
import io.udash.properties.model.ModelProperty
import srir.frontend.routing.CompileState
import srir.frontend.services.UserContextService
import srir.shared.model.SharedExceptions

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

    //get file form jQuery content
    //val msgProperty = model.subProp(_.data) //msgProperty.get should give content from input


    //use rest to send file

    //so far we are using implicit ec: ExecutionContext - we are needing section andThen in class
    userService.login("dupa","dupa").map(_ => ()).andThen {
      case Success(_) =>

      case Failure(_: SharedExceptions.UserNotFound) =>

      case Failure(_) =>

    }

  }

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
