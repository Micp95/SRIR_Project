package srir.frontend.views.compile

import io.udash.HasModelPropertyCreator
import org.scalajs.dom.File
import srir.shared.UploadedFile
import srir.shared.model.compile.CompileInformation


case class CompileModel(information: CompileInformation, data: String, selectedFile: Seq[File], uploadedFile: Seq[UploadedFile])

object CompileModel extends HasModelPropertyCreator[CompileModel]

