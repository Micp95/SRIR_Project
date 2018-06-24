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
      ApplicationContext.restServer.compileMethod().compileFile(name) onComplete{

        case Success(resp) =>{
          model.subProp(_.fileName).set(resp)
          executeFile()
        }

        case Failure(ex) =>{


        }

      }
    }
  }


  private def executeFile():Unit ={
    val name = model.subProp(_.fileName).get

    ApplicationContext.restServer.compileMethod().executeFile(name) onComplete{

      case Success(resp) =>{
        getStats()

      }

      case Failure(ex) =>{


      }

    }
  }

  private def getStats():Unit ={
    val name = model.subProp(_.fileName).get

    ApplicationContext.restServer.compileMethod().getStatsForFile(name) onComplete{

      case Success(resp) =>{

      }

      case Failure(ex) =>{

      }

    }
  }

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
