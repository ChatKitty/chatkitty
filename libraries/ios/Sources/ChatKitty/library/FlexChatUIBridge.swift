let kChatUiBaseUtl = "https://ui.chatkitty.com"

final class FlexChatUIBridge: @preconcurrency ChatUIBridge {
    
    private let component: FlexComponent
    
    @MainActor init(component: FlexComponent) {
        component.setBaseUrl(kChatUiBaseUtl)
        self.component = component
    }
    
    // MARK: ChatUIBridge
    
    @MainActor func initialize(options: InitializeOptions) {
        component.evalFlexFunc("initialize", sendData: options)
    }
    
    @MainActor func onChatUiConnected(_ onChatUiConnected: @escaping () -> Void) {
        component.setInterface("onChatUiConnected") { _ in
            onChatUiConnected()
        }
    }
    
    @MainActor func onChatMounted(_ onChatMounted: @escaping (ChatComponentContext) -> Void) {
        component.setInterface("onChatMounted") { (model: ChatComponentContext?) -> Any? in
            if let chatMountedContext = model {
                onChatMounted(chatMountedContext)
            }
            return nil
        }
    }
    
    @MainActor func onChatHeaderSelected(_ onChatHeaderSelected: @escaping (Channel) -> Void) {
        component.setInterface("onChatHeaderSelected") { (model: Channel?) -> Any? in
            if let channel = model {
                onChatHeaderSelected(channel)
            }
            return nil
        }
    }
    
    @MainActor func onChatMenuActionSelected(_ onChatMenuActionSelected: @escaping (MenuAction) -> Void) {
        component.setInterface("onChatMenuActionSelected") { (model: MenuAction?) -> Any? in
            if let menuAction = model {
                onChatMenuActionSelected(menuAction)
            }
            return nil
        }
    }
    
    @MainActor func onChatNotificationReceived(_ onChatNotificationReceived: @escaping (BaseNotification) -> Void) {
        component.setInterface("onChatNotificationReceived") { (model: BaseNotification?) -> Any? in
            if let notification = model {
                onChatNotificationReceived(notification)
            }
            return nil
        }
    }
    
    @MainActor func onPostMessage(_ onPostMessage: @escaping (ChatUIMessage) -> Void) {
        component.setInterface("postMessage")  { (model: ChatUIMessage?) -> Any? in
            if let message = model {
                onPostMessage(message)
            }
            return nil
        }
    }
}

