package srir.shared.rest

import com.avsystem.commons.serialization.transparent

@transparent
object CompileStatus extends Enumeration{
  type CompileStatus = Value
  val ConnectionFailed, ValidationFileFailed, CompileFileFailed,CompileSuccesfull = Value
}
