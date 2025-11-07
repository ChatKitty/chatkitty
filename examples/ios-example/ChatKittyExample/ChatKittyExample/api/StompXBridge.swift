import Foundation

protocol StompXBridge {
    func onMessage<T: Codable>(id: String?,
                               type: FlexStompXEventType,
                               payload: T)
    
    func onMessage(id: String?,
                   type: FlexStompXEventType)
}
