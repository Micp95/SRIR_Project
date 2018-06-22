package srir.backend.compile




import org.scalatest.{FlatSpec, Matchers}


class CodeLoaderTest extends FlatSpec with Matchers{


  it should "not be able to process file when error is in code" in {
    val fileContent =
      """new ExternalProcesss
        |  override def run() = add(10,20)
        |   def divide(a:Int,b:Int) = a + b
        |
      """.stripMargin

    try {

      val compiledFile = CodeLoader(fileContent)
      compiledFile.getFileReference
      false
    }catch {
      case ex: Exception => true
    }

  }


  it should "be able to process file if code is correct" in {
    val fileContent =
      """new ExternalProcesss {
        |  override def run() = add(10,20)
        |   def divide(a:Int,b:Int) = a + b
        |}
      """.stripMargin


    try {

      val compiledFile = CodeLoader(fileContent)
      compiledFile.getFileReference

      true
    }catch {
      case ex: Exception => false
    }
  }

}
