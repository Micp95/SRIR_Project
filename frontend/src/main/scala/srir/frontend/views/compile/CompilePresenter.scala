package srir.frontend.views.compile

import io.udash._
import io.udash.auth.AuthRequires
import io.udash.core.Presenter
import io.udash.properties.model.ModelProperty
import io.udash.utils.FileUploader
import org.scalajs.dom._
import srir.frontend.ApplicationContext
import srir.frontend.routing.CompileState
import srir.shared.ApplicationServerContexts
//import srir.shared.rest.CompileStatus

import scala.concurrent.ExecutionContext
import scala.util._

class CompilePresenter(model: ModelProperty[CompileModel])(implicit ec: ExecutionContext) extends Presenter[CompileState.type] with AuthRequires {

  private val uploader = new FileUploader(Url(ApplicationServerContexts.uploadContextPrefix))

  def processFile():Unit={

    model.subProp(_.compilerMessage).set("In progress...")
    model.subProp(_.executionMessage).set("Waiting")
    model.subProp(_.comparisonMessage).set("Waiting")
    uploader.upload("files", model.subSeq(_.selectedFile).get)

    val reader = new FileReader()

    reader.readAsText(model.subSeq(_.selectedFile).get.head)

    reader.onload = (e: UIEvent) => {

      val name = model.subSeq(_.selectedFile).get.head.name
      ApplicationContext.restServer.compileMethod().compileFile(name) onComplete{

        case Success(resp) =>{
          model.subProp(_.fileName).set(resp)
          model.subProp(_.compilerMessage).set("OK - file saved as: " + resp)
          executeFile()
        }

        case Failure(ex) =>{

          model.subProp(_.compilerMessage).set("ERROR - " + ex.getMessage.intern())
          model.subProp(_.executionMessage).set("Aborted")
          model.subProp(_.comparisonMessage).set("Aborted")

        }

      }
    }
  }


  private def executeFile():Unit ={

    model.subProp(_.executionMessage).set("In progress...")
    val name = model.subProp(_.fileName).get

    ApplicationContext.restServer.compileMethod().executeFile(name) onComplete{

      case Success(resp) =>{

        model.subProp(_.executionMessage).set("OK - result: " + resp)
        getStats()

      }

      case Failure(ex) =>{

        model.subProp(_.executionMessage).set("ERROR - " + ex.getMessage)
        model.subProp(_.comparisonMessage).set("Aborted")

      }

    }
  }

  private def getStats():Unit ={

    model.subProp(_.comparisonMessage).set("In progress...")

    val name = model.subProp(_.fileName).get

    ApplicationContext.restServer.compileMethod().getStatsForFile(name) onComplete{

      case Success(resp) =>{

        val stats=resp._2.mkString(", ")
        val statsdata=resp._3.mkString(", ")
        model.subProp(_.comparisonMessage).set("OK - result: " + resp._1+", ["+stats+"], ["+statsdata+"]")

      }

      case Failure(ex) =>{

        model.subProp(_.comparisonMessage).set("ERROR - " + ex.getMessage)

      }

    }
  }

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
