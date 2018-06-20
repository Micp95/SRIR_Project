package srir.shared.rpc.server.secure

import srir.shared.rpc.server.secure.chat.ChatRPC
import io.udash.rpc._

@RPC
trait SecureRPC {
  def chat(): ChatRPC
}

object SecureRPC extends DefaultServerUdashRPCFramework.RPCCompanion[SecureRPC]
