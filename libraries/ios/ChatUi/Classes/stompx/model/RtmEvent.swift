import Foundation

final class RtmEvent<T: Codable>: Codable {
    let type: String
    let resource: T?
}
