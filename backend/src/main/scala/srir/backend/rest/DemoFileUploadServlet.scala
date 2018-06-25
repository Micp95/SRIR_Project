package srir.backend.rest

import java.io.{File, InputStream}
import java.nio.file.Files

import io.udash.rpc._

class DemoFileUploadServlet(uploadDir: String) extends FileUploadServlet(Set("file", "files")) {
  new File(uploadDir).mkdir()

  override protected def handleFile(name: String, content: InputStream): Unit = {

    val targetFile = new File(uploadDir, name)
    Files.copy(content, targetFile.toPath)

  }

}
