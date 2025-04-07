import Foundation
import Moya

public protocol RelayResourceRequestable {
    func handleJSONMessage(client: StompX,
                           destination: String,
                           subscriptionId: String,
                           data: Data,
                           headers: [String : String])
}

public struct StompXRelayResourceRequestDictionary: RelayResourceRequestable {
    let destination: String
    let parameters: Dictionary<String, String>?
    let onSuccess: (Dictionary<String, Any>) -> Void
    let onError: (StompXError) -> Void
    
    init(destination: String,
         parameters: Dictionary<String, String>? = nil,
         onSuccess: @escaping (Dictionary<String, Any>) -> Void,
         onError: @escaping (StompXError) -> Void
    ) {
        self.destination = destination
        self.parameters = parameters
        self.onSuccess = onSuccess
        self.onError = onError
    }
    
    public func handleJSONMessage(client: StompX,
                                  destination: String,
                                  subscriptionId: String,
                                  data: Data,
                                  headers: [String : String]) {
        if let dict = data.toDictionary?["resource"] as? [String: Any]{
            onSuccess(dict)
        } else {
            onError(StompXError.parseError("Unable to parse mode JSON to Dictionary for destination: \(destination)"))
        }
    }
}

public struct StompXRelayResourceRequest<R: Codable>: RelayResourceRequestable {
    let destination: String
    let parameters: Dictionary<String, String>?
    let onSuccess: (R?) -> Void
    let onError: (StompXError) -> Void
    
    init(destination: String,
         parameters: Dictionary<String, String>? = nil,
         onSuccess: @escaping (R?) -> Void,
         onError: @escaping (StompXError) -> Void
    ) {
        self.destination = destination
        self.parameters = parameters
        self.onSuccess = onSuccess
        self.onError = onError
    }
    
    public func handleJSONMessage(client: StompX,
                                  destination: String,
                                  subscriptionId: String,
                                  data: Data,
                                  headers: [String : String]) {
        if let model = data.decode(to: RtmEvent<R>.self) {
            onSuccess(model.resource)
        } else {
            onError(StompXError.parseError("Unable to parse model of type \(String(describing: R.self)) for destination: \(destination)"))
        }
    }
}

public struct StompXListenToTopicRequest {
    let topic: String
    let onSuccess: (() -> Void)?
    let onNewData: ((Data) -> Void)?
    
    init(topic: String,
         onSuccess: (() -> Void)? = nil,
         onNewData: ((Data) -> Void)? = nil) {
        self.topic = topic
        self.onSuccess = onSuccess
        self.onNewData = onNewData
    }
}

public struct StompXListenForEventRequest<R: Codable> {
    let topic: String
    let event: String
    let onNewData: ((R) -> Void)?
    
    init(topic: String,
         event: String,
         onNewData: ((R) -> Void)? = nil) {
        self.topic = topic
        self.event = event
        self.onNewData = onNewData
    }
}

public struct StompXSendActionRequestDictionary<R: Codable>: RelayResourceRequestable {
    let destination: String
    let data: [String: Any]
    let onSent: (() -> Void)?
    let onSuccess: ((R) -> Void)?
    let onError: ((StompXError) -> Void)?
    
    init(destination: String,
         data: [String: Any],
         onSent: (() -> Void)? = nil,
         onSuccess: ((R) -> Void)? = nil,
         onError: ((StompXError) -> Void)? = nil) {
        self.destination = destination
        self.data = data
        self.onSent = onSent
        self.onSuccess = onSuccess
        self.onError = onError
    }
    
    public func handleJSONMessage(client: StompX,
                                  destination: String,
                                  subscriptionId: String,
                                  data: Data,
                                  headers: [String : String]) {
        // Actions only have errors
        if let model = data.decode(to: R.self, logError: false) {
            onSuccess?(model)
        } else if let model = data.decode(to: StompXServiceError.self) {
            onError?(StompXError.serviceError(model))
        }
    }
    
    public var jsonData: Data {
        do {
            return try JSONSerialization.data(withJSONObject: data, options: .prettyPrinted)
        } catch {
            return Data()
        }
    }
}

public struct SendToStreamRequest<R: Codable> {
    let stream: URL
    let grant: String
    let images: [UIImage]
    let onSuccess: ((R) -> Void)
    let onError: ((StompXError) -> Void)
    
    func handleMoyaResult(_ result: Result<Moya.Response, MoyaError>) {
        switch result {
        case let .success(moyaResponse):
            let data = moyaResponse.data
            if let model = data.decode(to: R.self) {
                onSuccess(model)
            } else {
                onError(StompXError.parseError("Unable to parse model of type \(String(describing: R.self)) for destination: \(stream.absoluteString)"))
            }
        case let .failure(error):
            onError(.moyaError(error))
        }
    }
}

public struct SendDataToStreamRequest<R: Codable> {
    let stream: URL
    let grant: String
    let data: [CreateDataFile]
    let onSuccess: ((R) -> Void)
    let onError: ((StompXError) -> Void)
    
    func handleMoyaResult(_ result: Result<Moya.Response, MoyaError>) {
        switch result {
        case let .success(moyaResponse):
            let data = moyaResponse.data
            if let model = data.decode(to: R.self) {
                onSuccess(model)
            } else {
                onError(StompXError.parseError("Unable to parse model of type \(String(describing: R.self)) for destination: \(stream.absoluteString)"))
            }
        case let .failure(error):
            onError(.moyaError(error))
        }
    }
}

public struct StompXSendActionRequest<R: Codable, P: Encodable>: RelayResourceRequestable {
    let destination: String
    let data: P
    let onSent: (() -> Void)?
    let onSuccess: ((R) -> Void)?
    let onError: ((StompXError) -> Void)?
    
    init(destination: String,
         data: P,
         onSent: (() -> Void)? = nil,
         onSuccess: ((R) -> Void)? = nil,
         onError: ((StompXError) -> Void)? = nil) {
        self.destination = destination
        self.data = data
        self.onSent = onSent
        self.onSuccess = onSuccess
        self.onError = onError
    }
    
    public func handleJSONMessage(client: StompX,
                                  destination: String,
                                  subscriptionId: String,
                                  data: Data,
                                  headers: [String : String]) {
        // Actions only have errors
        if let model = data.decode(to: R.self, logError: false) {
            onSuccess?(model)
        } else if let model = data.decode(to: StompXServiceError.self) {
            onError?(StompXError.serviceError(model))
        }
    }
    
    public var jsonData: Data {
        let encoder = JSONEncoder()
        if let encoded = try? encoder.encode(data) {
            return encoded
        }
        return Data()
    }
}

public struct StompXSubscription: RelayResourceRequestable {
    private let subscriptionId: String
    private let onNewMessage: ((Data) -> Void)?
    
    init(subscriptionId: String, onNewMessage: ((Data) -> Void)?) {
        self.subscriptionId = subscriptionId
        self.onNewMessage = onNewMessage
    }
    
    
    public func handleJSONMessage(client: StompX,
                                  destination: String,
                                  subscriptionId: String,
                                  data: Data,
                                  headers: [String : String]) {
        onNewMessage?(data)
    }
    
    
    func unsubscribe(from client: StompX) {
        client.unsubscribe(subscriptionId: subscriptionId)
    }
}

public protocol StompXEventHandlable {
    var id: String { get }
    func handleJSONMessage(data: Data)
}

public struct StompXEventHandler<R: Codable>: StompXEventHandlable {
    public let id = UUID().uuidString.lowercased()
    private let event: String
    private let onNewMessage: ((R) -> Void)?
    
    init(event: String,
         onNewMessage: ((R) -> Void)?) {
        self.event = event
        self.onNewMessage = onNewMessage
    }
    
    public func handleJSONMessage(data: Data) {
        if let model = data.decode(to: StompXEvent<R>.self, logError: false), model.type == event{
            onNewMessage?(model.resource)
        }
    }
}
