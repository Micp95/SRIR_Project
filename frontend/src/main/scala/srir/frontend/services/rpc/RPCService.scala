package srir.frontend.services.rpc

import srir.shared.rpc.client.MainClientRPC
import srir.shared.rpc.client.chat.ChatNotificationsRPC

class RPCService(notificationsCenter: NotificationsCenter) extends MainClientRPC {
  override val chat: ChatNotificationsRPC =
    new ChatService(notificationsCenter.msgListeners, notificationsCenter.connectionsListeners)
}