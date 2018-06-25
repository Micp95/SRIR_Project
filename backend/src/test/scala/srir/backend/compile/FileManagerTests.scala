package srir.backend.stats


import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import org.scalatest.{FlatSpec, Matchers}

class FileManagerTests extends FlatSpec with Matchers {

  val manager=new FileManager

  "FileManager" should "not be able count lines when path is wrong" in {
    val result=manager.countLines("wrong path")
    result should equal (0)
  }
  it should "not be able count words when path is wrong" in {
    val result=manager.countWords("wrong path")
    result should equal (0)
  }
  it should "not be able count characters when path is wrong" in {
    val result=manager.countChars("wrong path")
    result should equal (0)
  }
  it should "not be able to save file when is empty" in {
    val fileContent =""

    val filePath = "ExternalFile"
    Files.write(Paths.get(filePath), fileContent.getBytes(StandardCharsets.UTF_8))
    val result = manager.saveFile(filePath,filePath)
    new File(filePath).delete()

    result should include ("empty")
  }
  it should "not be able to compare when there is no files" in {
    val result = manager.compareFiles()
    result._1 should include ("no files")
  }
  it should "not be able to compare when there is 1 file" in {

    FileManager.fileCounter=FileManager.addcounter()
    val result = manager.compareFiles()
    result._1 should include ("only 1 file")
  }

}