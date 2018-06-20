package srir.frontend.services.rpc

import srir.shared.model.chat.ChatMessage
import srir.shared.rpc.client.chat.ChatNotificationsRPC
import io.udash.utils.CallbacksHandler

class ChatService(
  msgListeners: CallbacksHandler[ChatMessage],
  connectionsListeners: CallbacksHandler[Int]
) extends ChatNotificationsRPC {
  override def newMessage(msg: ChatMessage): Unit =
    msgListeners.fire(msg)

  override def connectionsCountUpdate(count: Int): Unit =
    connectionsListeners.fire(count)
}
