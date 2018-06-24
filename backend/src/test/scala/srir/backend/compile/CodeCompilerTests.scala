package srir.backend.compile
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import srir.shared.rest.CompileStatus._
import org.scalatest.{FlatSpec, Matchers}


class CodeCompilerTests extends FlatSpec with Matchers{

  val compiler = new CodeCompiler



  "CodeCompiler" should "not be able to process file, when file doesn't exist" in {
    compiler.processFile("invalid path").shouldBe(Left(WrongPath))
  }


  it should "not be able to process file when there is without ExternalProcess class" in {
    val fileContent =
      """new xddd {
        |  override def run() = add(10,20)
        |   def divide(a:Int,b:Int) = a + b
        |}
      """.stripMargin

    val filePath = "ExternalFile"
    Files.write(Paths.get(filePath), fileContent.getBytes(StandardCharsets.UTF_8))
    val result = compiler.processFile(filePath)
    new File(filePath).delete()
    result.shouldBe(Left(MissExternalProcess))
  }


  it should "not be able to process file when there is without run method" in {
    val fileContent =
      """new ExternalProcess {
        |  override def xddd() = add(10,20)
        |   def divide(a:Int,b:Int) = a + b
        |}
      """.stripMargin

    val filePath =  "ExternalFile"
    Files.write(Paths.get(filePath), fileContent.getBytes(StandardCharsets.UTF_8))
    val result = compiler.processFile(filePath)
    new File(filePath).delete()
    result.shouldBe(Left(MissRun))
  }

  it should "not be able to process file when error in code" in {
    val fileContent =
      """new ExternalProcess
        |  override def run() = add(10,20)
        |   def divide(a:Int,b:Int) = a + b
        |
      """.stripMargin

    val filePath =  "ExternalFile"
    Files.write(Paths.get(filePath), fileContent.getBytes(StandardCharsets.UTF_8))
    val result = compiler.processFile(filePath)
    new File(filePath).delete()
    result.shouldBe(Left(FailedCompilation))
  }


  it should "be able to process file if code is correct" in {
    val fileContent =
      """new ExternalProcess {
        |  override def run() = add(10,20)
        |   def add(a:Int,b:Int) = a + b
        |}
      """.stripMargin

    val filePath =  "ExternalFile"
    Files.write(Paths.get(filePath), fileContent.getBytes(StandardCharsets.UTF_8))
    val result = compiler.processFile(filePath)
    new File(filePath).delete()

    result.isRight.shouldBe(true)

    val runResult = result.right.get.run()
    runResult.shouldBe(30)

  }
}
