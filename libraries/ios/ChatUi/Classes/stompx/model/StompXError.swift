import Moya
public enum StompXError: Error {
    case parseError(String)
    case serviceError(StompXServiceError)
    case moyaError(MoyaError)
    
    public var localizedDescription: String {
        switch self {
        case .serviceError(let error):
            return error.message
        case .parseError(let error):
            return error
        case .moyaError(let error):
            return error.localizedDescription
        }
    }
}

public class StompXServiceError: Codable {
    public let error: String
    public let message: String
    public let timestamp: String
}
