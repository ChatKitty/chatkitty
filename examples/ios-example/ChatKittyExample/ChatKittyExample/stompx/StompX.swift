import Foundation

public protocol StompX {
    var isConnected: Bool { get }

    func connect(request: StompXConnectRequest)
    func disconnect(completion: @escaping () -> Void)
    func relayResource<T: Decodable>(request: StompXRelayResourceRequest<T>)
    func relayResourceDictionary(request: StompXRelayResourceRequestDictionary)
    func sendAction<T: Decodable, P: Encodable>(request: StompXSendActionRequest<T, P>)
    func sendActionDictionary<T: Decodable>(request: StompXSendActionRequestDictionary<T>)
    func sendToStream<T: Decodable>(request: SendToStreamRequest<T>)
    func sendToStream<T: Decodable>(request: SendDataToStreamRequest<T>)
    func listenToTopic(request: StompXListenToTopicRequest) -> () -> Void
    func listenForEvent<T: Decodable>(request: StompXListenForEventRequest<T>) -> () -> Void
    
    func sendJSONMessage(destination: String, data: Data, headers: [String : String])
    func subscribe(destination: String, headers: [String : String]) -> String
    func unsubscribe(subscriptionId: String)

    func sendFrame(_ frame: StompClientFrame)
    func resignToClient(client: StompX)
}
