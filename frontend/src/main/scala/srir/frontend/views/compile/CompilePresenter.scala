package srir.frontend.views.compile

import java.util.concurrent.TimeUnit

import io.udash._
import io.udash.auth.AuthRequires
import io.udash.core.Presenter
import io.udash.properties.model.ModelProperty
import io.udash.utils.FileUploader
import prickle.Unpickle
import srir.frontend.ApplicationContext
import srir.frontend.routing.CompileState
import srir.shared.ApplicationServerContexts
import srir.shared.rest.{CompileResponse, ExecutionResponse}
import scala.concurrent.ExecutionContext
import scala.util._

class CompilePresenter(model: ModelProperty[CompileModel])(implicit ec: ExecutionContext) extends Presenter[CompileState.type] with AuthRequires {
  private val uploader = new FileUploader(Url(ApplicationServerContexts.uploadContextPrefix))
  private var _name = ""

  def upload(): Unit = {

    uploader.upload("files", model.subSeq(_.selectedFile).get)
    _name = model.subSeq(_.selectedFile).get.head.name

  }

  def processFile(): Unit = {

    model.subProp(_.compilerMessage).set("In progress...")
    model.subProp(_.executionMessage).set("Waiting")
    model.subProp(_.comparisonMessage).set("Waiting")

    val startTime = System.nanoTime

    ApplicationContext.restServer.compileMethod().compileFile(_name) onComplete {

      case Success(resp) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)
        val result = Unpickle[CompileResponse].fromString(resp).get

        if (result.errorMessage == null) {
          model.subProp(_.fileName).set(result.fileName)
          model.subProp(_.compilerMessage).set(s"($diffTime) OK - file saved as: ${result.fileName}")
          executeFile()
        } else {
          model.subProp(_.compilerMessage).set(s"($diffTime) ERROR - ${result.errorMessage}")
          model.subProp(_.executionMessage).set("Aborted")
          model.subProp(_.comparisonMessage).set("Aborted")
        }
      }

      case Failure(ex) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)


        model.subProp(_.compilerMessage).set(s"($diffTime) ERROR - ${ex.getMessage}")
        model.subProp(_.executionMessage).set("Aborted")
        model.subProp(_.comparisonMessage).set("Aborted")

      }

    }
  }


  private def executeFile(): Unit = {

    model.subProp(_.executionMessage).set("In progress...")
    val name = model.subProp(_.fileName).get

    val startTime = System.nanoTime
    ApplicationContext.restServer.compileMethod().executeFile(name) onComplete {


      case Success(resp) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)
        val result = Unpickle[ExecutionResponse].fromString(resp).get

        if (result.errorMessage == null) {
          model.subProp(_.executionMessage).set(s"($diffTime) OK - result: ${result.result}")
          getStats()
        } else {
          model.subProp(_.executionMessage).set(s"($diffTime)  ERROR - ${result.errorMessage}")
          model.subProp(_.comparisonMessage).set("Aborted")
        }

      }

      case Failure(ex) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)
        model.subProp(_.executionMessage).set(s"($diffTime) ERROR - ${ex.getMessage}")
        model.subProp(_.comparisonMessage).set("Aborted")

      }

    }
  }

  private def getStats(): Unit = {

    model.subProp(_.comparisonMessage).set("In progress...")

    val name = model.subProp(_.fileName).get

    val startTime = System.nanoTime
    ApplicationContext.restServer.compileMethod().getStatsForFile(name) onComplete {

      case Success(resp) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)

        val stats = resp._2.mkString(", ")
        val statsdata = resp._3.mkString(", ")
        model.subProp(_.comparisonMessage).set(s"($diffTime) OK - result: ${resp._1}, [$stats], [$statsdata]")

      }

      case Failure(ex) => {
        val diffTime = TimeUnit.NANOSECONDS.toNanos(System.nanoTime - startTime)

        model.subProp(_.comparisonMessage).set(s"($diffTime) ERROR - " + ex.getMessage)

      }

    }
  }

  def name_= (value:String):Unit = _name = value

  override def handleState(state: CompileState.type): Unit = {

  }

  override def onClose(): Unit = {

  }

}
