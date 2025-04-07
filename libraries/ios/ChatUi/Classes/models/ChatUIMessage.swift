import Foundation

final class ChatUIMessage: Codable {
    let type: String
    let id: String?
    let payload: AnyCodable?
}

extension Encodable {
    func toJSONData() -> Data? {
        try? JSONEncoder().encode(self)
    }

    func toJSONString(prettyPrinted: Bool = false) -> String {
        let encoder = JSONEncoder()
        if prettyPrinted {
            encoder.outputFormatting = .prettyPrinted
        }
        guard let jsonData = try? encoder.encode(self) else { return "" }
        return String(data: jsonData, encoding: .utf8) ?? ""
    }
}
