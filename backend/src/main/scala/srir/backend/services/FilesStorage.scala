package srir.backend.services

import srir.shared.UploadedFile

import scala.collection.mutable

object FilesStorage {
  private val files: mutable.AnyRefMap[String,UploadedFile] = mutable.AnyRefMap.empty

  def add(file: UploadedFile): Unit =
    files.+=(file.fileName,file)

  def allFiles: List[UploadedFile] =
    files.valuesIterator.toList

  def getFile (fileName:String): UploadedFile=
    files(fileName)

}
