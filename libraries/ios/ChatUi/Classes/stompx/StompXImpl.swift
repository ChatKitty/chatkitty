import Foundation
import Starscream
import RxSwift
import Moya

public typealias StompXImpl = WebsocketStompClient

public final class WebsocketStompClient : StompX, WebSocketDelegate {

    private let service = MoyaProvider<StompXStream>()
    
    public var isConnected: Bool = false

    private let specification: StompSpecification = StompSpecification()

    private var disconnectReceipt: String = ""

    private var frameQueue: FrameQueue = FrameQueue()

    private let configuration: StompXConfiguration
    
    private var _socket: WebSocket?
    
    private var socket: WebSocket {
        guard let socket = _socket else {
            fatalError("Socket not created!!!")
        }
        return socket
    }
    
    private var url: URL!
    private var stompXRequest: StompXConnectRequest!
    
    // MARK: Subscriptions
    private let disposeBag = DisposeBag()
    private let onConnectedSubject = PublishSubject<Void>()
    private let onDisconnectedSubject = PublishSubject<Void>()
    private let watchForReceiptSubject = PublishSubject<String>()
    private let watchForErrorsSubject = PublishSubject<Error>()
    
    private let pendingRelayRequests = ThreadSafeDictionary<String, RelayResourceRequestable>()
    private let pendingActionRequests = ThreadSafeDictionary<String, RelayResourceRequestable>()
    private let eventHandlers = ThreadSafeDictionary<String, [StompXEventHandlable]>()
    
    private var subscriptionToDisposeBag = Dictionary<String, DisposeBag>()
    
    private var version: String {
        if let bundle = Bundle.allFrameworks.filter({ $0.bundleIdentifier == "org.cocoapods.ChatKitty" }).first,
           let version = bundle.infoDictionary?["CFBundleShortVersionString"] as? String {
            return version
        }
        return "0.0.0"
    }
    
    public init(configuration: StompXConfiguration) {
        self.configuration = configuration
    }

    public func connect(request: StompXConnectRequest) {
        let wsScheme = configuration.isSecure ? "wss" : "ws"
        guard let url = URL(string: "\(wsScheme)://\(configuration.host)/rtm/websocket?api-key=\(request.apiKey)") else { fatalError("Invalid url") }
        self.url = url
        self.stompXRequest = request
        var urlRequest = URLRequest(url: url)
        if let authParams = request.authParams {
            urlRequest.setValue(ObjectMapper.writeValueAsString(dictionary: authParams)?.base64Encoded,
                                forHTTPHeaderField: "StompX-Auth-Params")
        }
        _socket = WebSocket(request: urlRequest)
        _socket?.delegate = self
        socket.connect()
        onConnectedSubject.take(1).subscribe(onNext: { _ in
            request.onConnected()
        }).disposed(by: disposeBag)
    }
    
    public func send(message: SocketMessage) {
        sendJSONMessage(destination: message.destination, data: message.data)
    }
    
    public func sendJSONMessage(destination: String, data: Data, headers: [String : String] = [:]) {
        do {
            try sendFrame(specification.sendJSONMessage(destination: destination, data: data, headers: headers))
        } catch {
            // TODO - parse error for receipt
        }
    }

    // MARK: STOMPX Implementation
    
    public func relayResourceDictionary(request: StompXRelayResourceRequestDictionary) {
        let id = specification.generateSubscriptionId()
        
        pendingRelayRequests[id] = request
        
        sendFrame(specification.subscribe(id: id,
                                          destination: request.destination,
                                          headers: request.parameters ?? [:]))
    }
    
    public func relayResource<T: Decodable>(request: StompXRelayResourceRequest<T>) {
        let id = specification.generateSubscriptionId()
        
        pendingRelayRequests[id] = request
        
        sendFrame(specification.subscribe(id: id,
                                          destination: request.destination,
                                          headers: request.parameters ?? [:]))
    }
    
    public func sendActionDictionary<T>(request: StompXSendActionRequestDictionary<T>) where T : Decodable, T : Encodable {
        let receipt = specification.generateReceipt()
        
        if let onSent = request.onSent {
            watchForReceiptSubject
                .filter{$0 == receipt}
                .take(1)
                .subscribe(onNext: { _ in
                onSent()
            }).disposed(by: disposeBag)
        }
        
        pendingActionRequests[receipt] = request
        
        sendJSONMessage(destination: request.destination,
                        data: request.jsonData,
                        headers: ["receipt" : receipt])
    }
    
    public func sendAction<T, P>(request: StompXSendActionRequest<T, P>) where T : Decodable, P : Encodable {
        let receipt = specification.generateReceipt()
        
        if let onSent = request.onSent {
            watchForReceiptSubject
                .filter{$0 == receipt}
                .take(1)
                .subscribe(onNext: { _ in
                onSent()
            }).disposed(by: disposeBag)
        }
        
        pendingActionRequests[receipt] = request
        
        sendJSONMessage(destination: request.destination,
                        data: request.jsonData,
                        headers: ["receipt" : receipt])
    }

    public func sendToStream<T>(request: SendToStreamRequest<T>) where T : Decodable, T : Encodable {
        service.request(.uploadImages(url: request.stream,
                                      grant: request.grant,
                                      images: request.images)) { result in
            request.handleMoyaResult(result)
        }
    }
    
    public func sendToStream<T>(request: SendDataToStreamRequest<T>) where T : Decodable, T : Encodable {
        service.request(.uploadFiles(url: request.stream,
                                      grant: request.grant,
                                      data: request.data)) { result in
            request.handleMoyaResult(result)
        }
    }
    
    public func listenToTopic(request: StompXListenToTopicRequest) -> () -> Void {
        let subscriptionReceipt = specification.generateReceipt()
        if let onSuccess = request.onSuccess {
            watchForReceiptSubject
                .take(1)
                .filter({ $0 == subscriptionReceipt})
                .subscribe(onNext: { _ in
                    onSuccess()
                }).disposed(by: disposeBag)
        }
        let id = specification.generateSubscriptionId()
        
        sendFrame(specification.subscribe(id: id, destination: request.topic, headers: ["receipt" : subscriptionReceipt]))
        
        return { [weak self] in
            guard let self = self else { return }
            unsubscribe(subscriptionId: id)
        }
    }
    
    public func listenForEvent<T: Decodable>(request: StompXListenForEventRequest<T>) -> () -> Void {
        let handlers = eventHandlers[request.topic] ?? []
        
        let handler = StompXEventHandler<T>(event: request.event) { model in
            request.onNewData?(model)
        }
        
        eventHandlers[request.topic] = handlers + [handler]
        
        return { [weak self] in
            guard let self = self else { return }
            self.eventHandlers[request.topic]?.removeAll(where: { $0.id == handler.id })
        }
    }
    
    // MARK: STOMP Implementation
    
    public func subscribe(destination: String, headers: [String : String] = [:]) -> String {
        let id = specification.generateSubscriptionId()

        sendFrame(specification.subscribe(id: id, destination: destination, headers: headers))

        return id
    }

    public func unsubscribe(subscriptionId: String) {
        sendFrame(specification.unsubscribe(subscriptionId: subscriptionId))
    }

    public func disconnect(completion: @escaping () -> Void) {
        sendFramesToClient(client: self)

        disconnectReceipt = specification.generateReceipt()

        sendFrame(specification.disconnect(receipt: disconnectReceipt))
        
        onDisconnectedSubject
            .take(1)
            .subscribe(onNext: { _ in
            completion()
        }).disposed(by: disposeBag)
    }

    public func sendFrame(_ frame: StompClientFrame) {
        if isConnected {
            socket.write(string: frame.description)
        } else {
            frameQueue.enqueue(frame)
        }
    }

    public func resignToClient(client: StompX) {
        sendFramesToClient(client: client)

        socket.disconnect()
    }

    private func websocketDidConnect(socket: WebSocketClient) {
        isConnected = true
        var stompXAuthParams: String? = nil
        if let authParams = stompXRequest.authParams {
            stompXAuthParams = ObjectMapper.writeValueAsString(dictionary: authParams)?.base64Encoded
        }
        sendFrame(specification.connect(host: url.host!,
                                        stompXUser: stompXRequest.username,
                                        stompXUserAgent: "ChatKitty-iOS/\(version)",
                                        stompXAuthParams: stompXAuthParams))
    }

    private func websocketDidDisconnect(socket: WebSocketClient, error: Error?) {
        isConnected = false
    }

    public func didReceive(event: WebSocketEvent, client: WebSocketClient) {
//        StompXLogger.logDebug("Did get event \(event)")
        switch event {
        case .connected(_):
            websocketDidConnect(socket: client)
        case .disconnected(_, _):
            onDisconnectedSubject.onNext(())
            websocketDidDisconnect(socket: client, error: nil)
        case .text(let text):
            websocketDidReceiveMessage(socket: client, text: text)
        case .binary(let data):
            websocketDidReceiveData(socket: client, data: data)
        case .ping(_):
            break
        case .pong(_):
            break
        case .viabilityChanged(_):
            break
        case .reconnectSuggested(_):
            break
        case .cancelled:
            isConnected = false
        case .error(_):
            isConnected = false
        case .peerClosed:
            isConnected = false
        }
    }
    
    public func websocketDidReceiveMessage(socket: WebSocketClient, text: String) {
        do {
            let components = text.components(separatedBy: "\n")

            if components.first == "" {
                sendHeartBeat()
            } else {
                let frame = try StompServerFrame(text: text)

                switch frame.command {
                case .connected:
                    onConnectedSubject.onNext(())
                    sendFramesToClient(client: self)
                case .message:
                    var headers: [String : String] = [:]

                    for header in frame.headers.values {
                        headers[header.key] = header.value
                    }
                    if frame.getHeader("content-type").contains("application/json") {
                        if let data = frame.body.data(using: .utf8) {
                            let id = frame.getHeader("subscription")
                            if let pendingRelayRequest = pendingRelayRequests[id] {
                                pendingRelayRequests.removeValue(forKey: id)
                                pendingRelayRequest.handleJSONMessage(client: self,
                                                                      destination: frame.getHeader("destination"), subscriptionId: id,
                                                                      data: data, headers: headers)
                            }
                            
                            if frame.containsHeader("destination") {
                                let destination = frame.getHeader("destination")
                                if let handlers = self.eventHandlers[destination] {
                                    handlers.forEach {
                                        $0.handleJSONMessage(data: data)
                                    }
                                }
                            }
                            
                            if frame.containsHeader("receipt-id") {
                                let receiptId = frame.getHeader("receipt-id")
                                
                                if let pendingActionRequest = pendingActionRequests[receiptId] {
                                    pendingActionRequests.removeValue(forKey: receiptId)
                                    pendingActionRequest.handleJSONMessage(client: self,
                                                                          destination: frame.getHeader("destination"), subscriptionId: id,
                                                                          data: data, headers: headers)
                                }
                            }
                        }
                    }
                case .receipt:
                    let receiptId = frame.getHeader("receipt-id")
                    watchForReceiptSubject.onNext(receiptId)
                    if receiptId == disconnectReceipt {
                        socket.disconnect()
                    }
                case .error:
                    watchForErrorsSubject.onNext(NSError(domain: "com.chatkitty.ios.sockets", code: 1000, userInfo: [:]))
                }
            }
        } catch {
            watchForErrorsSubject.onNext(error)
        }
    }

    public func websocketDidReceiveData(socket: WebSocketClient, data: Data) {
        // Not called in STOMP
    }

    private func sendHeartBeat() {
        socket.write(string: specification.generateHeartBeat())
    }

    private func sendFramesToClient(client: StompX) {
        while !frameQueue.isEmpty {
            client.sendFrame(frameQueue.dequeue()!)
        }
    }
}

final class OfflineStompClient : StompX {
    func sendToStream<T>(request: SendDataToStreamRequest<T>) where T : Decodable, T : Encodable {
        // TODO
    }
    
    func sendToStream<T>(request: SendToStreamRequest<T>) where T : Decodable, T : Encodable {
        // TODO
    }
    
    func sendActionDictionary<T>(request: StompXSendActionRequestDictionary<T>) where T : Decodable, T : Encodable {
        // TODO
    }
    
    func relayResourceDictionary(request: StompXRelayResourceRequestDictionary) {
        // TODO
    }
    
    func listenToTopic(request: StompXListenToTopicRequest) -> () -> Void {
        // TODO
        return {}
    }
    
    
    func sendAction<T, P>(request: StompXSendActionRequest<T, P>) where T : Decodable, T : Encodable, P : Encodable {
        // TODO
    }
    
    func relayResource<T>(request: StompXRelayResourceRequest<T>) where T : Decodable, T : Encodable {
        // TODO
    }
    
    public func listenForEvent<T>(request: StompXListenForEventRequest<T>) -> () -> Void {
        return  {}
    }
    
    func disconnect(completion: @escaping () -> Void) {
        // TODO
    }
    
    
    public var isConnected: Bool = false

    private let specification: StompSpecification = StompSpecification()

    private var frameQueue: FrameQueue = FrameQueue()
    
    public func connect(request: StompXConnectRequest) { }

    public func sendJSONMessage(destination: String, data: Data, headers: [String : String] = [:]) {
        do {
            try sendFrame(specification.sendJSONMessage(destination: destination, data: data))
        } catch {}
    }

    public func subscribe(destination: String, headers: [String : String] = [:]) -> String {
        let id = specification.generateSubscriptionId()

        sendFrame(specification.subscribe(id: id, destination: destination, headers: headers))

        return id
    }

    public func unsubscribe(subscriptionId: String) {
        sendFrame(specification.unsubscribe(subscriptionId: subscriptionId))
    }

    public func sendFrame(_ frame: StompClientFrame) {
        frameQueue.enqueue(frame)
    }

    public func resignToClient(client: StompX) {
        while !frameQueue.isEmpty {
            client.sendFrame(frameQueue.dequeue()!)
        }
    }
}

fileprivate struct FrameQueue {
    private var array = [StompClientFrame?]()
    private var head = 0

    public var isEmpty: Bool {
        return count == 0
    }

    public var count: Int {
        return array.count - head
    }

    public mutating func enqueue(_ element: StompClientFrame) {
        array.append(element)
    }

    public mutating func dequeue() -> StompClientFrame? {
        guard head < array.count, let element = array[head] else { return nil }

        array[head] = nil
        head += 1

        let percentage = Double(head)/Double(array.count)
        if array.count > 50 && percentage > 0.25 {
            array.removeFirst(head)
            head = 0
        }

        return element
    }

    public var front: StompClientFrame? {
        if isEmpty {
            return nil
        } else {
            return array[head]
        }
    }
}


public struct StompClientFrame: CustomStringConvertible {
    private(set) var command: StompClientCommand
    private(set) var headers: Set<StompHeader>
    private(set) var body: String

    init(command: StompClientCommand, headers: Set<StompHeader> = [], body: String = "") {
        self.command = command
        self.headers = headers
        self.body = body
    }

    public var description: String {
        var string = command.rawValue + "\n"

        for header in headers {
            string += header.key + ":" + header.value + "\n"
        }

        string += "\n" + body + "\0"

        return string
    }
}

struct StompServerFrame: CustomStringConvertible {
    private(set) var command: StompServerCommand
    private(set) var headers: [String : StompHeader]
    private(set) var body: String

    private init(command: StompServerCommand, headers: [String : StompHeader], body: String) {
        self.command = command
        self.headers = headers
        self.body = body
    }

    init(text: String) throws {
        var components = text.components(separatedBy: "\n")

        if components.first == "" {
            components.removeFirst()
        }

        let command = try StompServerCommand(text: components.first!)

        var headers: [String:StompHeader] = [:]
        var body = ""
        var isBody = false
        for index in 1 ..< components.count {
            let component = components[index]
            if isBody {
                body += component
                if body.hasSuffix("\0") {
                    body = body.replacingOccurrences(of: "\0", with: "")
                }
            } else {
                if component == "" {
                    isBody = true
                } else {
                    let parts = component.components(separatedBy: ":")

                    guard let key = parts.first, let value = parts.last else {
                        continue
                    }

                    headers[key] = StompHeader(key: key, value: value)
                }
            }
        }

        self.init(command: command, headers: headers, body: body)
    }

    var description: String {
        var string = command.rawValue + "\n"

        for header in headers.values {
            string += header.key + ":" + header.value + "\n"
        }

        string += "\n" + body + "\0"

        return string
    }

    
    func containsHeader(_ header: String) -> Bool {
        return headers[header]?.value != nil
    }
    
    func getHeader(_ header: String) -> String {
        return (headers[header]?.value)!
    }
}

enum StompClientCommand: String {
    case send = "SEND"
    case subscribe = "SUBSCRIBE"
    case unsubscribe = "UNSUBSCRIBE"
    case begin = "BEGIN"
    case commit = "COMMIT"
    case abort = "ABORT"
    case ack = "ACK"
    case nack = "NACK"
    case disconnect = "DISCONNECT"
    case connect = "CONNECT"
    case stomp = "STOMP"


    init(text: String) throws {
        guard let command = StompClientCommand(rawValue: text) else {
            throw NSError(domain: "com.chatkitty.ios.sockets", code: 1001, userInfo: [NSLocalizedDescriptionKey : "Sent command is undefined."])
        }

        self = command
    }
}

enum StompServerCommand: String {
    case connected = "CONNECTED"
    case message = "MESSAGE"
    case receipt = "RECEIPT"
    case error = "ERROR"

    init(text: String) throws {
        guard let command = StompServerCommand(rawValue: text) else {
            throw NSError(domain: "com.chatkitty.ios.sockets", code: 1002, userInfo: [NSLocalizedDescriptionKey : "Received command is undefined."])
        }

        self = command
    }
}


enum StompHeader: Hashable {
    case contentLength(length: Int)
    case contentType(type: String)
    case receipt(receipt: String)

    case acceptVersion(version: String)
    case host(host: String)
    case login(login: String)
    case passcode(passcode: String)
    case heartBeat(value: String)

    case version(version: String)
    case session(session: String)
    case server(server: String)

    case destination(destination: String)
    case transaction(transaction: String)

    case id(id: String)
    case ack(ack: String)

    case messageId(id: String)
    case subscription(id: String)

    case receiptId(id: String)

    case custom(key: String, value: String)

    init(key: String, value: String) {
        switch key {
        case "content-length":
            self = .contentLength(length: Int(value)!)
        case "content-type":
            self = .contentType(type: value)
        case "receipt":
            self = .receipt(receipt: value)
        case "accept-version":
            self = .acceptVersion(version: value)
        case "host":
            self = .host(host: value)
        case "login":
            self = .login(login: value)
        case "passcode":
            self = .passcode(passcode: value)
        case "heart-beat":
            self = .heartBeat(value: value)
        case "version":
            self = .version(version: value)
        case "session":
            self = .session(session: value)
        case "server":
            self = .server(server: value)
        case "destination":
            self = .destination(destination: value)
        case "transaction":
            self = .transaction(transaction: value)
        case "id":
            self = .id(id: value)
        case "ack":
            self = .ack(ack: value)
        case "message-id":
            self = .messageId(id: value)
        case "subscription":
            self = .subscription(id: value)
        case "receipt-id":
            self = .receiptId(id: value)
        default:
            self = .custom(key: key, value: value)
        }
    }

    var key: String {
        switch self {
        case .contentLength:
            return "content-length"
        case .contentType:
            return "content-type"
        case .receipt:
            return "receipt"
        case .acceptVersion:
            return "accept-version"
        case .host:
            return "host"
        case .login:
            return "login"
        case .passcode:
            return "passcode"
        case .heartBeat:
            return "heart-beat"
        case .version:
            return "version"
        case .session:
            return "session"
        case .server:
            return "server"
        case .destination:
            return "destination"
        case .transaction:
            return "transaction"
        case .id:
            return "id"
        case .ack:
            return "ack"
        case .messageId:
            return "message-id"
        case .subscription:
            return "subscription"
        case .receiptId:
            return "receipt-id"
        case .custom(let key, _):
            return key
        }
    }

    var value: String {
        switch self {
        case .custom(_, let value):
            return value
        case .contentLength(let length):
            return "\(length)"
        case .contentType(let type):
            return type
        case .receipt(let receipt):
            return receipt
        case .acceptVersion(let version):
            return version
        case .host(let host):
            return host
        case .login(let login):
            return login
        case .passcode(let passcode):
            return passcode
        case .heartBeat(let value):
            return value
        case .version(let version):
            return version
        case .session(let session):
            return session
        case .server(let server):
            return server
        case .destination(let destination):
            return destination
        case .transaction(let transaction):
            return transaction
        case .id(let id):
            return id
        case .ack(let ack):
            return ack
        case .messageId(let id):
            return id
        case .subscription(let id):
            return id
        case .receiptId(let id):
            return id
        }
    }

    func hash(into hasher: inout Hasher) {
        hasher.combine(key.hashValue)
    }

    static func ==(lhs: StompHeader, rhs: StompHeader) -> Bool {
        return lhs.hashValue == rhs.hashValue
    }
}


public struct SocketMessage {
    public let destination: String
    public let headers: [String : String]
    public let data: Data

    public init(destination: String,
                headers: [String : String] = [:],
                data: Data = Data()) {
        self.destination = destination
        self.headers = headers
        self.data = data
    }
    
    public init(destination: String, headers: [String:String], object: Codable) {
        do {
            let data = try JSONSerialization.data(withJSONObject: object, options: JSONSerialization.WritingOptions())
            self.init(destination: destination, headers: headers, data: data)
        } catch {
            self.init(destination: destination, headers: headers)
        }
    }
}
