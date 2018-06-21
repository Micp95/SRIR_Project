package srir.shared.rest

import io.udash.rest.REST

@REST
trait MainServerREST {
  def compileMethod():CompilerServerREST

}
