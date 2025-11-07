import Foundation

public final class ChatKittyFile: Codable {
    public let type: String
    public let url: String
    public let name: String
    public let contentType: String
    public let size: Double
    
    public init(type: String,
                url: String,
                name: String,
                contentType: String,
                size: Double) {
        self.type = type
        self.url = url
        self.name = name
        self.contentType = contentType
        self.size = size
    }
}

public enum File {
    case data(CreateDataFile)
    case external(CreateChatKittyExternalFileProperties)
}

public final class CreateDataFile {
    public let data: Data
    public let contentType: String
    public let name: String?
    
    public init(data: Data,
                contentType: String,
                name: String? = nil) {
        self.data = data
        self.contentType = contentType
        self.name = name
    }
}

public final class CreateChatKittyExternalFileProperties: Codable {
    public let url: String
    public let name: String
    public let contentType: String
    public let size: Double
    
    public init(url: String,
                name: String,
                contentType: String,
                size: Double
    ) {
        self.url = url
        self.name = name
        self.contentType = contentType
        self.size = size
    }
}

public enum ChatKittyUploadResult {
    case completed
    case failed
    case cancelled
}
