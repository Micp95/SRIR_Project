package srir.frontend.views.compile


//import java.util.UUID

import io.udash._
import io.udash.auth.AuthRequires
import io.udash.core.Presenter
import io.udash.properties.model.ModelProperty
import io.udash.utils.FileUploader
import org.scalajs.dom._
import srir.frontend.ApplicationContext
import srir.frontend.routing.CompileState
import srir.shared.ApplicationServerContexts

import scala.concurrent.ExecutionContext
import scala.util._

class CompilePresenter(
                     model: ModelProperty[CompileModel]
                   )(
                    implicit ec: ExecutionContext
                   ) extends Presenter[CompileState.type] with AuthRequires {

  private val uploader = new FileUploader(Url(ApplicationServerContexts.uploadContextPrefix))



  def processFile():Unit={
    uploader.upload("files", model.subSeq(_.selectedFile).get)

    val reader = new FileReader()

    reader.readAsText(model.subSeq(_.selectedFile).get.head)

    reader.onload = (e: UIEvent) => {

      val name = model.subSeq(_.selectedFile).get.head.name
      ApplicationContext.restServer.compileMethod().sendFile(name) onComplete{

        case Success(xd) =>{
          executeFile()

        }

        case Failure(ex) =>

      }
    }
  }



  private def executeFile():Unit ={
    val name = model.subSeq(_.selectedFile).get.head.name

    ApplicationContext.restServer.compileMethod().executeFile(name) onComplete{

      case Success(xd) =>

      case Failure(ex) =>

    }
  }


  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
