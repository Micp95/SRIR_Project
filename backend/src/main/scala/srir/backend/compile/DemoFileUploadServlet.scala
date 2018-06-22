package srir.backend.compile

import java.io.{File, InputStream}
import java.nio.file.Files
//import java.util.UUID

import io.udash.rpc._
//import srir.backend.services.FilesStorage
//import srir.shared.UploadedFile

class DemoFileUploadServlet(uploadDir: String) extends FileUploadServlet(Set("file", "files")) {
  new File(uploadDir).mkdir()

  override protected def handleFile(name: String, content: InputStream): Unit = {

    //val targetName: String = s"${UUID.randomUUID()}_${name.replaceAll("[^a-zA-Z0-9.-]", "_")}"

    val targetFile = new File(uploadDir, name)
    Files.copy(content, targetFile.toPath)
    //FilesStorage.add(
      //UploadedFile(name, targetName, targetFile.length())
    //)

  }

}
