package srir.frontend.views.compile

import io.udash.HasModelPropertyCreator
import org.scalajs.dom.File
import srir.shared.UploadedFile


case class CompileModel(data: String,fileName: String, selectedFile: Seq[File], uploadedFile: Seq[UploadedFile])

object CompileModel extends HasModelPropertyCreator[CompileModel]

