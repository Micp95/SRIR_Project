package srir.shared.rpc.client

import srir.shared.rpc.client.chat.ChatNotificationsRPC
import io.udash.rpc._

@RPC
trait MainClientRPC {
  def chat(): ChatNotificationsRPC
}

object MainClientRPC extends DefaultClientUdashRPCFramework.RPCCompanion[MainClientRPC]