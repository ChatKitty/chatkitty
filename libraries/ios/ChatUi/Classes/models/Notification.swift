public struct BaseNotification: Codable {
    let id: Int
    let title: String
    let body: String
    let channel: Channel?
    var data: AnyCodable
    let muted: Bool
    let createdTime: String
    var readTime: String?
}
