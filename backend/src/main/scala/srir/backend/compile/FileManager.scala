package srir.backend.compile

import scala.io.Source
import java.io.File
import java.io.PrintWriter

class FileManager {
  var fileCounter :Int = 0
  val pathName :String = "file"
  val fileExtension=".txt"



  def countLines(file: String): Int ={
    val src = Source.fromFile(file)
    val count = src.getLines.size
    println(count)
    return count
  }
  def countWords(file: String): Int = {
    val src = Source.fromFile(file)
    val count =
      (for {
        line <- src.getLines
      } yield {
        val words = line.split("\\s+")
        words.size
      }).sum
    println(count)
    return count
  }
  def countChars(file: String): Int={
    val src = Source.fromFile(file)
    val count =
      (for {
        line <- src.getLines
      } yield {
        val chars = line.split(" ").flatMap(_.toCharArray)
        chars.size
      }).sum
    println(count)
    return count
  }
  def saveFile(file: String):String ={
    //val rx = """(?m)\s*^(\++|-+)$\s*"""
    //val filelines = text.split(rx)
    val fileLines=Source.fromFile(file).getLines()
    if(fileLines.isEmpty){
      return "file is empty, cannot save"
    }
    fileCounter=fileCounter+1
    val writer = new PrintWriter(new File(pathName+fileCounter+fileExtension))
    fileLines.foreach { x =>writer.write(x+"\n") }
    writer.close()
    return "file saved"
  }
  def compareFiles(): (String, Array[Int])={
    val results=Array(0,0,0)
    var msg="good"
    if(fileCounter==0){
      msg="there is no files"
      return (msg,results)
    }
    if(fileCounter==1){
        msg="there is only 1 file to compare"
        return (msg,results)
    }
    val previous=pathName+(fileCounter-1)+fileExtension
    val current= pathName+fileCounter+fileExtension

    val difflines=this.countLines(current)-this.countLines(previous)
    val diffwords=this.countWords(current)-this.countWords(previous)
    val diffchars=this.countChars(current)-this.countChars(previous)

    return (msg,Array(difflines,diffwords,diffchars))

  }



}
