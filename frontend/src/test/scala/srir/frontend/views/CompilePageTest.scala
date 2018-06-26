package srir.frontend.views

import io.udash.properties.model.ModelProperty
import org.scalatest.{AsyncFlatSpec, Matchers}
import srir.frontend.views.compile.{CompileModel, CompilePresenter}

import scala.concurrent.Future

class CompilePageTest extends AsyncFlatSpec with Matchers{

  val correctFile =
    """
      |new ExternalProcess {
      |  override def run() = add(10,20)
      |   def add(a:Int,b:Int) = a + b
      |}
    """.stripMargin

  val incorrectFile =
    """
      |new ExternalProcesss {
      |	override def xddd() = add(10,20)
      |		def add(a:Int,b:Int) = a + b
      |}
    """.stripMargin

  val incorrenctProgram =
    """
      |new ExternalProcesss {
      |	override def run() = add(10,20)
      |		def add(a:Int,b:Int) = (a + b)/0
      |}
    """.stripMargin

  "CompilePage" should "have messages empty on page init" in {

      val model = ModelProperty(CompileModel("", "", "", "", Seq.empty))

      val presenter = new CompilePresenter(model)
      presenter.onClose()

      Future {

        model.subSeq(_.selectedFile).get should be(null)
        model.subProp(_.fileName).get should be("")
        model.subProp(_.comparisonMessage).get should be("")
        model.subProp(_.compilerMessage).get should be("")
        model.subProp(_.executionMessage).get should be("")

      }

    }

    it should "not compile if file is invalid" in {

      val model = ModelProperty(CompileModel("", "", "", "", Seq.empty))
      val presenter = new CompilePresenter(model)
      presenter.name_= (incorrectFile)
      presenter.processFile()

      Future {

        model.subProp(_.compilerMessage).get should include("ERROR -")
        model.subProp(_.executionMessage).get should be("Aborted")
        model.subProp(_.comparisonMessage).get should be("Aborted")

      }

    }

  it should "not execute if program is incorrect" in {

    val model = ModelProperty(CompileModel("", "", "", "", Seq.empty))
    val presenter = new CompilePresenter(model)
    presenter.name_= (incorrenctProgram)
    presenter.processFile()


    Future {

      model.subProp(_.compilerMessage).get should include("OK - file saved as:")
      model.subProp(_.executionMessage).get should include("ERROR -")
      model.subProp(_.comparisonMessage).get should be("Aborted")

    }

  }

  it should "run if file is correct" in {

    val model = ModelProperty(CompileModel("", "", "", "", Seq.empty))
    val presenter = new CompilePresenter(model)
    presenter.name_= (correctFile)
    presenter.processFile()

    Future {

      model.subProp(_.compilerMessage).get should include("OK - file saved as:")
      model.subProp(_.executionMessage).get should include("OK - result:")
      model.subProp(_.comparisonMessage).get should include("OK - result:")

    }

  }

  it should "not run run if file is null" in {

    val model = ModelProperty(CompileModel("", "", "", "", Seq.empty))
    val presenter = new CompilePresenter(model)
    presenter.processFile()

    Future {

      model.subProp(_.compilerMessage).get should include("ERROR -")
      model.subProp(_.executionMessage).get should be("Aborted")
      model.subProp(_.comparisonMessage).get should be("Aborted")

    }

  }

}
