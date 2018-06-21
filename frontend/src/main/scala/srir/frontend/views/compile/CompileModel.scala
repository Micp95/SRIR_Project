package srir.frontend.views.compile

import io.udash.HasModelPropertyCreator
import srir.shared.model.compile.CompileInformation


case class CompileModel(information: CompileInformation, data:String)

object CompileModel extends HasModelPropertyCreator[CompileModel]

