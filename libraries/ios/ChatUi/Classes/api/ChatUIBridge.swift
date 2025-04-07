/**
 export interface Flex {
   onChatUiConnected: () => Promise<void>
   onChatMounted: (context: ChatComponentContext) => Promise<void>
   onChatHeaderSelected: (channel: Channel) => Promise<void>
   onChatMenuActionSelected: (action: MenuAction) => Promise<void>
   onChatNotificationReceived: (notification: Notification) => Promise<void>
 }
 */
protocol ChatUIBridge {
    func initialize(options: InitializeOptions)
    
    func onChatUiConnected(_ onChatUiConnected: @escaping () -> Void)
    
    func onChatMounted(_ onChatMounted: @escaping (ChatComponentContext) -> Void)
    
    func onChatHeaderSelected(_ onChatHeaderSelected: @escaping (Channel) -> Void)
    
    func onChatMenuActionSelected(_ onChatMenuActionSelected: @escaping (MenuAction) -> Void)
    
    func onChatNotificationReceived(_ onChatNotificationReceived: @escaping (BaseNotification) -> Void)
    
    func onPostMessage(_ onPostMessage: @escaping (ChatUIMessage) -> Void)
}
