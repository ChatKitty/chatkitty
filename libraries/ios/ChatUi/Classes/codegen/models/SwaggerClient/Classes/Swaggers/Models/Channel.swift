//
// Channel.swift
//
// Generated by swagger-codegen
// https://github.com/swagger-api/swagger-codegen
//

import Foundation



public struct Channel: Codable {

    public var lastReceivedMessage: Message?
    public var _id: Int?
    public var properties: AnyCodable?
    public var creator: Member?
    public var type: String?
    public var createdTime: String?

    public init(lastReceivedMessage: Message? = nil, _id: Int? = nil, properties: AnyCodable? = nil, creator: Member? = nil, type: String? = nil, createdTime: String? = nil) {
        self.lastReceivedMessage = lastReceivedMessage
        self._id = _id
        self.properties = properties
        self.creator = creator
        self.type = type
        self.createdTime = createdTime
    }

    public enum CodingKeys: String, CodingKey { 
        case lastReceivedMessage
        case _id = "id"
        case properties
        case creator
        case type
        case createdTime
    }

}
