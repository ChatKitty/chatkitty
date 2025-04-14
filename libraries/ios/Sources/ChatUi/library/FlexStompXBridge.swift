final class FlexStompXBridge: StompXBridge {
    private let component: FlexComponent
    
    init(component: FlexComponent) {
        component.setBaseUrl(kChatUiBaseUtl)
        self.component = component
    }
    
    func onMessage<T: Codable>(id: String?,
                               type: FlexStompXEventType,
                               payload: T) {
        let message = FlexStompXMessage(id: id,
                                    type: type.rawValue,
                                    payload: payload)
        let encoded = message.toJSONString().data(using: .utf8)?.base64EncodedString()
        if encoded != nil {
            component.evalFlexFunc("onMessage",
                                   sendData: encoded!)
        }
    }
    
    func onMessage(id: String?,
                   type: FlexStompXEventType) {
        component.evalFlexFunc("onMessage",
                               sendData: FlexStompXEmptyMessage(id: id,
                                                           type: type.rawValue))
    }
    
}
