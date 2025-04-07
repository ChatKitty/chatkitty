let kChatUiBaseUtl = "https://ui.chatkitty.com"

final class FlexChatUIBridge: ChatUIBridge {
    
    private let component: FlexComponent
    
    init(component: FlexComponent) {
        component.setBaseUrl(kChatUiBaseUtl)
        self.component = component
    }
    
    // MARK: ChatUIBridge
    
    func initialize(options: InitializeOptions) {
        component.evalFlexFunc("initialize", sendData: options)
    }
    
    func onChatUiConnected(_ onChatUiConnected: @escaping () -> Void) {
        component.setInterface("onChatUiConnected") { _ in
            onChatUiConnected()
        }
    }
    
    func onChatMounted(_ onChatMounted: @escaping (ChatComponentContext) -> Void) {
        component.setInterface("onChatMounted") { (model: ChatComponentContext?) -> Any? in
            if let chatMountedContext = model {
                onChatMounted(chatMountedContext)
            }
            return nil
        }
    }
    
    func onChatHeaderSelected(_ onChatHeaderSelected: @escaping (Channel) -> Void) {
        component.setInterface("onChatHeaderSelected") { (model: Channel?) -> Any? in
            if let channel = model {
                onChatHeaderSelected(channel)
            }
            return nil
        }
    }
    
    func onChatMenuActionSelected(_ onChatMenuActionSelected: @escaping (MenuAction) -> Void) {
        component.setInterface("onChatMenuActionSelected") { (model: MenuAction?) -> Any? in
            if let menuAction = model {
                onChatMenuActionSelected(menuAction)
            }
            return nil
        }
    }
    
    func onChatNotificationReceived(_ onChatNotificationReceived: @escaping (BaseNotification) -> Void) {
        component.setInterface("onChatNotificationReceived") { (model: BaseNotification?) -> Any? in
            if let notification = model {
                onChatNotificationReceived(notification)
            }
            return nil
        }
    }
    
    func onPostMessage(_ onPostMessage: @escaping (ChatUIMessage) -> Void) {
        component.setInterface("postMessage")  { (model: ChatUIMessage?) -> Any? in
            if let message = model {
                onPostMessage(message)
            }
            return nil
        }
    }
}

