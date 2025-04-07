
public struct StompXConnectRequest {
    public let apiKey: String
    public let username: String
    public let authParams: Dictionary<String, Any>?
    public let onConnected: () -> Void
    public let onConnectionLost: () -> Void
    public let onConnectionResumed: () -> Void
    public let onError: (StompXError) -> Void
}
