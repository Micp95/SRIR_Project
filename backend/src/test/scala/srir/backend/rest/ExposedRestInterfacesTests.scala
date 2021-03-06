package srir.backend.rest

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalatest.{FlatSpec, Matchers}
import srir.backend.services.FilesStorage
import srir.shared.UploadedFile
import srir.shared.model.compile.ExternalProcess
import srir.shared.rest.{CompileResponse, ExecutionResponse}

class ExposedRestInterfacesTests extends FlatSpec with Matchers {

  implicit val formats = DefaultFormats

  val path = "xdUnitTest"
  val fileName = "unitTestFile.tst"
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



  new File(path).mkdir()


  "mv" should "be able to copy file" in {

    if(Files.exists(Paths.get(s"$path/$fileName")))
      new File(s"$path/$fileName").delete()

    val newFileName = fileName +"new"
    Files.write(Paths.get(s"$path/$fileName"), correctFile.getBytes(StandardCharsets.UTF_8))

    val api = new ExposedRestInterfaces(path)
    api.mv(s"$path/$fileName",s"$path/$newFileName")


    Files.exists(Paths.get(s"$path/$newFileName")).shouldBe(true)

    new File(s"$path/$newFileName").delete()
  }


  //COMPILE TESTS

  "ExposedRestInterfaces - compile" should "not be able to process when file doesn't exist" in {

    if(Files.exists(Paths.get(s"$path/$fileName")))
      new File(s"$path/$fileName").delete()

    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().compileFile(fileName)

    val resultBody = result.value.head.get

    val parsedObject = parse(resultBody).extract[CompileResponse]

    parsedObject.errorMessage.contains("WrongPath").shouldBe(true)
  }

  it should "not be able to process when file is incorrect" in {

    if(Files.exists(Paths.get(s"$path/$fileName")))
      new File(s"$path/$fileName").delete()

    Files.write(Paths.get(s"$path/$fileName"), incorrectFile.getBytes(StandardCharsets.UTF_8))

    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().compileFile(fileName)

    val resultBody = result.value.head.get

    val parsedObject = parse(resultBody).extract[CompileResponse]

    parsedObject.errorMessage.contains("Compile failed").shouldBe(true)

    new File(s"$path/$fileName").delete()
  }

  it should "be able to process when file is correct" in {

    if(Files.exists(Paths.get(s"$path/$fileName")))
      new File(s"$path/$fileName").delete()

    Files.write(Paths.get(s"$path/$fileName"), correctFile.getBytes(StandardCharsets.UTF_8))

    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().compileFile(fileName)

    val resultBody = result.value.head.get

    val parsedObject = parse(resultBody).extract[CompileResponse]

    parsedObject.errorMessage.shouldBe(null)
    parsedObject.fileName.contains(fileName).shouldBe(true)

    new File(s"$path/$fileName").delete()
    new File(s"$path/${parsedObject.fileName}").delete()
  }

  //EXECUTION TESTS

  "ExposedRestInterfaces - execution" should "not be able to execute when compiled file doesn't exist" in {


    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().executeFile("xdddd")

    val resultBody = result.value.head.get

    val parsedObject = parse(resultBody).extract[ExecutionResponse]

    parsedObject.errorMessage.contains("null")
  }


  it should "be able to execute when application is correct" in {

    val compiledFile = new  ExternalProcess {
      override def run() = add(10,20)
      def add(a:Int,b:Int) = a + b
    }

    FilesStorage.add(
      UploadedFile(fileName, compiledFile)
    )

    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().executeFile(fileName)

    val resultBody = result.value.head.get
    val parsedObject = parse(resultBody).extract[ExecutionResponse]



    parsedObject.errorMessage.shouldBe(null)
    parsedObject.result.shouldBe(30)

  }

  it should "not be able to execute when application is incorrect" in {

    val compiledFile = new  ExternalProcess {
      override def run() = add(10,20)
      def add(a:Int,b:Int) = (a + b)/0
    }

    FilesStorage.add(
      UploadedFile(fileName, compiledFile)
    )

    val api = new ExposedRestInterfaces(path)
    val result = api.compileMethod().executeFile(fileName)

    val resultBody = result.value.head.get
    val parsedObject = parse(resultBody).extract[ExecutionResponse]


    parsedObject.errorMessage.contains("Execution failed")

  }



}
