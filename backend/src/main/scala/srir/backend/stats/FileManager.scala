package srir.backend.stats

import java.io.{File, PrintWriter}

import scala.io.Source

class FileManager {

  val pathName :String = "file"
  val fileExtension=""
  var filesNames = Array[String]()

  def addOneToCounter()={
    FileManager.fileCounter=FileManager.addcounter()
  }
  def countLines(file: String): Int ={
    if (!new File(file).exists()){
      return 0
    }
    val src = Source.fromFile(file)
    val count = src.getLines.size
    println(count)
    return count
  }
  def countWords(file: String): Int = {
    if (!new File(file).exists()){
      return 0
    }
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
    if (!new File(file).exists()){
      return 0
    }
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
  def saveFile(filepath: String, name:String):String ={
    //val rx = """(?m)\s*^(\++|-+)$\s*"""
    //val filelines = text.split(rx)

    val fileLines=Source.fromFile(filepath).getLines()
    if(fileLines.isEmpty){
      return "file is empty, cannot save"
    }
    FileManager.fileCounter=FileManager.addcounter()
    println(FileManager.fileCounter)
    val writer = new PrintWriter(new File(pathName+FileManager.fileCounter+fileExtension))
    fileLines.foreach { x =>writer.write(x+"\n") }
    writer.close()
    return "file saved"
  }
  def compareFiles(): (String,Array[String], Array[Int])={
    val results=Array(-1,-1,-1)
    var msg="good"
    val stats=Array("difflines", "diffwords","diffchars")
    if(FileManager.fileCounter==0){
      msg="there is no files"
      return (msg,stats,results)
    }
    if(FileManager.fileCounter==1){
        msg="there is only 1 file to compare"
        return (msg,stats,results)
    }
    //val filepaths=filesNames.toArray

    val previous=pathName+(FileManager.fileCounter-1)+fileExtension
    val current= pathName+FileManager.fileCounter+fileExtension

    val difflines=this.countLines(current)-this.countLines(previous)
    println(difflines)
    val diffwords=this.countWords(current)-this.countWords(previous)
    println(diffwords)
    val diffchars=this.countChars(current)-this.countChars(previous)
    println(diffchars)

    return (msg, stats,Array(difflines,diffwords,diffchars))

  }



}
object FileManager{
  var fileCounter :Int = 0
  def addcounter()=fileCounter+1
}
