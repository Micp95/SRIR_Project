package srir.shared.model.compile

import com.avsystem.commons.serialization.{HasGenCodec}

case class CompileInformation(fileName: String, author: String,fileData : String)

object CompileInformation extends HasGenCodec[CompileInformation]

