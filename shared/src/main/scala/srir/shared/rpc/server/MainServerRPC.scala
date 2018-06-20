package srir.shared.rpc.server

import srir.shared.model.auth.UserToken
import srir.shared.rpc.server.open.AuthRPC
import srir.shared.rpc.server.secure.SecureRPC
import io.udash.i18n._
import io.udash.rpc._

@RPC
trait MainServerRPC {
  /** Returns an RPC for authentication. */
  def auth(): AuthRPC

  /** Verifies provided UserToken and returns a [[srir.shared.rpc.server.secure.SecureRPC]] if the token is valid. */
  def secure(token: UserToken): SecureRPC

  /** Returns an RPC serving translations from the server resources. */
  def translations(): RemoteTranslationRPC
}

object MainServerRPC extends DefaultServerUdashRPCFramework.RPCCompanion[MainServerRPC]