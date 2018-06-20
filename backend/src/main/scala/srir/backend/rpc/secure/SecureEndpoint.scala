package srir.backend.rpc.secure

import srir.backend.rpc.secure.chat.ChatEndpoint
import srir.backend.services.DomainServices
import srir.shared.model.auth.UserContext
import srir.shared.rpc.server.secure.SecureRPC
import srir.shared.rpc.server.secure.chat.ChatRPC

class SecureEndpoint(implicit domainServices: DomainServices, ctx: UserContext) extends SecureRPC {
  import domainServices._

  lazy val chatEndpoint = new ChatEndpoint

  override def chat(): ChatRPC = chatEndpoint
}
