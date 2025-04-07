import Foundation

final class StompSpecification {
    public func connect(host: String,
                        stompXUser: String,
                        stompXUserAgent: String,
                        stompXAuthParams: String?
    ) -> StompClientFrame {
        var headers: Set<StompHeader> = [.acceptVersion(version: "1.2"),
                                         .host(host: host),
                                         .heartBeat(value: "10000,10000"),
                                         .custom(key: "StompX-User", value: stompXUser),
                                         .custom(key: "StompX-User-Agent", value: stompXUserAgent)]

        if let stompXAuthParams {
            headers.insert(.custom(key: "StompX-Auth-Params", value: stompXAuthParams))
        }
        return StompClientFrame(command: .connect, headers: headers)
    }

    public func subscribe(id: String, destination: String, headers: [String : String] = [:]) -> StompClientFrame {
        var stompHeaders: Set<StompHeader> = [.id(id: id), .destination(destination: destination), .ack(ack: "client-individual")]

        for (key, value) in headers {
            stompHeaders.insert(.custom(key: key, value: value))
        }

        return StompClientFrame(command: .subscribe, headers: stompHeaders)
    }
    
    public func sendJSONMessage(destination: String, data: Data, headers: [String : String] = [:]) throws -> StompClientFrame {
        let message = String(data: data, encoding: String.Encoding.utf8)!
        
        var stompHeaders: Set<StompHeader> = [.destination(destination: destination), .contentType(type: "application/json;charset=UTF-8"), .contentLength(length: message.utf8.count)]

        for (key, value) in headers {
            stompHeaders.insert(.custom(key: key, value: value))
        }

        return StompClientFrame(command: .send, headers: stompHeaders, body: message)
    }

    public func unsubscribe(subscriptionId: String) -> StompClientFrame {
        let headers: Set<StompHeader> = [.id(id: subscriptionId)]

        return StompClientFrame(command: .unsubscribe, headers: headers)
    }

    public func ack(messageId: String) -> StompClientFrame {
        let headers: Set<StompHeader> = [.id(id: messageId)]

        return StompClientFrame(command: .ack, headers: headers)
    }

    public func disconnect(receipt: String) -> StompClientFrame {
        let headers: Set<StompHeader> = [.receipt(receipt: receipt)]

        return StompClientFrame(command: .disconnect, headers: headers)
    }

    public func generateReceipt() -> String {
        return "receipt-" + UUID().uuidString.lowercased()
    }

    public func generateSubscriptionId() -> String {
        return "subscription-id-" + UUID().uuidString.lowercased()
    }

    public func generateHeartBeat() -> String {
        return "\n\n"
    }
}
