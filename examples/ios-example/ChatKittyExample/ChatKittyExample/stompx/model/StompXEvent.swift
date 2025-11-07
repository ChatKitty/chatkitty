import Foundation

public class StompXEvent<T: Codable>: Codable {
    public let type: String
    public let resource: T
}
