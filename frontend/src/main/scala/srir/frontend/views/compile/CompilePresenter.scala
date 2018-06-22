package srir.frontend.views.compile


import io.udash._
import io.udash.auth.AuthRequires
import io.udash.properties.model.ModelProperty
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

  def sendFile(): Unit ={

    uploader.upload("files", model.subSeq(_.selectedFile).get)

    val reader = new FileReader()

    reader.readAsText(model.subSeq(_.selectedFile).get.head)

    reader.onload = (e: UIEvent) => {
      val contents = reader.result.asInstanceOf[String]

      val name = model.subSeq(_.selectedFile).get.head.name

      val size = model.subSeq(_.selectedFile).get.head.size

      val message = "{\"name\":\"" + name + "\",\"size\":\"" + size + "\",\"content\":\"" + contents + "\"}"

      ApplicationContext.restServer.compileMethod().sendFile(message) onComplete{

        case Success(xd) =>

        case Failure(ex) =>

      }

    }



  }

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
