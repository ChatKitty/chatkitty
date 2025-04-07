import Foundation

final class StompXPage<T: Codable>: Codable {
    let _embedded: T?
    let page: StompXPageMetadata
    let _relays: StompXRelays?
}

final class StompXPageMetadata: Codable {
    let size: Int
    let totalElements: Int?
    let totalPages: Int?
    let number: Int?
}

final class StompXRelays: Codable {
    let this: String
    let first: String?
    let prev: String?
    let next: String?
    let last: String?

    private enum CodingKeys : String, CodingKey {
        case this = "self"
        case first
        case last
        case prev
        case next
    }
}
