package srir.shared

import com.avsystem.commons.serialization.HasGenCodec
import io.udash.properties.ModelPropertyCreator

case class UploadedFile(name: String, serverFileName: String, size: Long)
object UploadedFile extends HasGenCodec[UploadedFile] {
  implicit val mpc: ModelPropertyCreator[UploadedFile] = ModelPropertyCreator.materialize
}

