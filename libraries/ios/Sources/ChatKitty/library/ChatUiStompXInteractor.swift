
final class ChatUIStompXInteractor {
    private let stompX: StompX
    private let stompXBridge: StompXBridge
    private var subscriptionMap: [String : () -> Void] = [:]
    
    init(stompX: StompX, stompXBridge: StompXBridge) {
        self.stompX = stompX
        self.stompXBridge = stompXBridge
    }
    
    func onReceiveMessage(event: ChatUIMessage) {
//        print("[flex] " + event.toJSONString())
        switch event.type {
        case "stompx:connect":
            var writeGrant: String? = nil
            var readGrant: String? = nil
            
            self.stompX.relayResource(request: StompXRelayResourceRequest<AnyCodable>(
                destination: "/application/v1/user.relay",
                onSuccess: { user in
                self.stompX.relayResource(request: StompXRelayResourceRequest<ChatKittyGrant>(
                    destination: "/application/v1/user.write_file_access_grant.relay",
                    onSuccess: { write in
                        writeGrant = write?.grant
                        self.stompX.relayResource(request: StompXRelayResourceRequest<ChatKittyGrant>(
                            destination: "/application/v1/user.read_file_access_grant.relay",
                            onSuccess: { read in
                            readGrant = read?.grant
                                if let user {
                                    self.stompXBridge.onMessage(id: nil,
                                                                type: .connectSuccess,
                                                                payload: ConnectPayload(user: user,
                                                                                        write: writeGrant,
                                                                                        read: readGrant))
                                }
                        }, onError: { error in
                            self.stompXBridge.onMessage(id: nil,
                                                        type: .connectFailure,
                                                        payload: AnyCodable(nil))
                        }))
                    }, onError: { error in
                        self.stompXBridge.onMessage(id: nil,
                                                    type: .connectFailure,
                                                    payload: AnyCodable(nil))
                    }))
            }, onError: { error in
                self.stompXBridge.onMessage(id: nil,
                                            type: .connectFailure,
                                            payload: AnyCodable(nil))
            }))
        case "stompx:resource.relay":
            if let relayPayload = event.payload?.synthesize(to: StompXRelayPayload.self) {
                self.stompX.relayResource(request: StompXRelayResourceRequest<AnyCodable>(
                    destination: relayPayload.destination,
                    parameters: relayPayload.parameters?.mapValues { $0 ? "true" : "false" } ?? [:],
                    onSuccess: { model in
                        guard let model else {
                            return
                        }
                        self.stompXBridge.onMessage(id: event.id,
                                                    type: .relaySuccess,
                                                    payload: StompXResource(resource: model))
                }, onError: { error in
                    self.stompXBridge.onMessage(id: event.id,
                                                type: .relayError,
                                                payload: StompXResource(resource: AnyCodable(nil)))
                }))
            }
        case "stompx:topic.subscribe":
            if let subscribePayload = event.payload?.synthesize(to: StompXSubscribePayload.self) {
                let subscription = self.stompX.listenToTopic(request: StompXListenToTopicRequest(topic: subscribePayload.topic, onSuccess: {
                    self.stompXBridge.onMessage(id: event.id,
                                                type: .topicSubscribed)
                }))
                subscriptionMap[event.id ?? ""] = subscription
            }
        case "stompx:event.listen":
            if let listenForEventPayload = event.payload?.synthesize(to: StompXListenForEventPayload.self) {
                let subscription = self.stompX.listenForEvent(request: StompXListenForEventRequest<AnyCodable>(topic: listenForEventPayload.topic,
                                                                                        event: listenForEventPayload.event,
                                                                                        onNewData: { model in
                    self.stompXBridge.onMessage(id: event.id,
                                                type: .eventPublished,
                                                payload: StompXResource(resource: model))
                }))
                
                subscriptionMap[event.id ?? ""] = subscription
            }
        case "stompx:action.perform":
            if let performPayload = event.payload?.synthesize(to: StompXPerformActionPayload.self) {
                self.stompX.sendAction(request: StompXSendActionRequest<AnyCodable, AnyCodable>(destination: performPayload.destination,
                                                                                                data: performPayload.body ?? AnyCodable(nil), onSent: {
                    self.stompXBridge.onMessage(id: event.id,
                                                type: .actionSent,
                                                payload: StompXResource(resource: AnyCodable(nil)))
                }, onSuccess: { resource in
                    self.stompXBridge.onMessage(id: event.id,
                                                type: .actionSuccess,
                                                payload: StompXResource(resource: resource))
                }))
            }
        case "stompx:topic.unsubscribe",
             "stompx:event.unsubscribe":
            if let id = event.id {
                subscriptionMap[id]?()
            }
        case "stompx:disconnect":
            subscriptionMap.forEach { $0.value() }
            subscriptionMap.removeAll()
        default:
            // TODO: Log to Sentry of an unhandled event
            break
        }
    }
}
