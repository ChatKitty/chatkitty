//
//  FlexData.swift
//  FlexHybridApp
//
//  Created by dvkyun on 2020/09/01.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation

public class FlexData {
    private let data: Any?
    public let type: DataType
    
    public enum DataType {
        case NIL
        case STRING
        case INT
        case DOUBLE
        case FLOAT
        case BOOL
        case ARRAY
        case DICTIONARY
        case ERR
    }
    
    internal init() {
        data = nil
        type = DataType.NIL
    }
    
    internal init(_ data: String) {
        self.data = data
        type = DataType.STRING
    }
    
    internal init(_ data: Int) {
        self.data = data
        type = DataType.INT
    }
    
    internal init(_ data: Double) {
        self.data = data
        type = DataType.DOUBLE
    }
    
    internal init(_ data: Float) {
        self.data = data
        type = DataType.FLOAT
    }
    
    internal init(_ data: Bool) {
        self.data = data
        type = DataType.BOOL
    }
    
    internal init(_ data: [FlexData]) {
        self.data = data
        type = DataType.ARRAY
    }
    
    internal init(_ data: [String:FlexData]) {
        self.data = data
        type = DataType.DICTIONARY
    }
    
    internal init(_ data: BrowserException) {
        self.data = data
        type = DataType.ERR
    }
    
    public func isNil() -> Bool {
        return data == nil
    }
    
    public func asString() -> String? {
        if isNil() { return nil }
        if(type != DataType.STRING) {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
        return (data as! String)
    }
    
    public func asInt() -> Int? {
        if isNil() { return nil }
        switch type {
        case DataType.INT:
            return (data as! Int)
        case DataType.DOUBLE:
            return Int((data as! Double))
        case DataType.FLOAT:
            return Int((data as! Float))
        default:
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
    }
    
    public func asFloat() -> Float? {
        if isNil() { return nil }
        switch type {
        case DataType.INT:
            return Float((data as! Int))
        case DataType.DOUBLE:
            return Float((data as! Double))
        case DataType.FLOAT:
            return (data as! Float)
        default:
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
    }
    
    public func asDouble() -> Double? {
        if isNil() { return nil }
        switch type {
        case DataType.INT:
            return Double((data as! Int))
        case DataType.DOUBLE:
            return (data as! Double)
        case DataType.FLOAT:
            return Double((data as! Float))
        default:
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
    }
    
    public func asBool() -> Bool? {
        if isNil() { return nil }
        if(type != DataType.BOOL) {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
        return (data as! Bool)
    }
    
    public func asArray() -> [FlexData]? {
        if isNil() { return nil }
        if(type != DataType.ARRAY) {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
        return (data as! [FlexData])
    }
    
    public func asDictionary() -> [String:FlexData]? {
        if isNil() { return nil }
        if(type != DataType.DICTIONARY) {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
        return (data as! [String:FlexData])
    }
    
    public func asErr() -> BrowserException? {
        if isNil() { return nil }
        if(type != DataType.ERR) {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
        return (data as! BrowserException)
    }
    
    public func toString() -> String? {
        switch type {
        case DataType.NIL: return nil
        case DataType.STRING: return asString()!
        case DataType.INT: return String(asInt()!)
        case DataType.DOUBLE: return String(asDouble()!)
        case DataType.FLOAT: return String(asFloat()!)
        case DataType.ERR: return asErr()!.reason
        case DataType.BOOL: return String(asBool()!)
        case DataType.ARRAY:
            guard let array = asArray() else {
                return nil
            }
            var result = "["
            for value in array {
                result += "\(value.toString() ?? "nil"),"
            }
            result += "]"
            return result
        case DataType.DICTIONARY:
            guard let dic = asDictionary() else {
                return nil
            }
            var result = "{"
            for (name, value) in dic {
                result += "\(name):\(value.toString() ?? "nil"),"
            }
            result += "}"
            return result
        }
    }
    
    
    
    public func reified<T>() -> T? {
        if isNil() { return nil }
        if (T.self == Int.self) {
            return (asInt() as? T)
        } else if(T.self == Float.self) {
            return (asFloat() as? T)
        } else if(T.self == Double.self) {
            return (asDouble() as? T)
        } else if(T.self == String.self) {
            return (toString() as? T)
        } else if(T.self == Bool.self) {
            return (asBool() as? T)
        } else if(T.self == [FlexData].self) {
            return (asArray() as? T)
        } else if(T.self == [String:FlexData].self) {
            return (asDictionary() as? T)
        } else if(T.self == BrowserException.self) {
            return (asErr() as? T)
        } else {
            FlexMsg.err(FlexString.ERROR8)
            return nil
        }
    }
}
