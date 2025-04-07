//
//  FlexFunc.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/11/25.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation

struct FlexFunc {
    static func convertValue(_ value: Any?) throws -> String {
        if value == nil || value is NSNull {
            return "null"
        } else if let _value = value as? Bool {
            if _value {
                return "true"
            } else {
                return "false"
            }
        } else if value is Int || value is Double || value is Float {
            return "\(value!)"
        } else if value is String || value is Character {
            return "`\(value!)`"
        } else if let _vArray = value as? [Any?] {
            var _vString = "["
            for e in _vArray {
                if let _value = e as? Bool {
                    if _value {
                        _vString.append("true,")
                    } else {
                        _vString.append("false,")
                    }
                } else if e is Int || e is Double || e is Float {
                    _vString.append("\(e!),")
                } else if e is String || e is Character {
                    _vString.append("`\(e!)`,")
                } else if e is [Any?] || e is [String:Any?] {
                    _vString.append("\(try convertValue(e)),")
                } else if e == nil || e is NSNull {
                    _vString.append("null,")
                } else {
                    throw FlexError.UnuseableTypeCameIn
                }
            }
            _vString.append("]")
            return _vString
        } else if let _vDic = value as? [String:Any?] {
            var _vString = "{"
            for (_name, e) in _vDic {
                if let _value = e as? Bool {
                    if _value {
                        _vString.append("\(_name):true,")
                    } else {
                        _vString.append("\(_name):false,")
                    }
                } else if e is Int || e is Double || e is Float {
                    _vString.append("\(_name):\(e!),")
                } else if e is String || e is Character {
                    let eEscaped = "\(e!)".replacingOccurrences(of: "`", with: "\\`")
                    _vString.append("\(_name):`\(eEscaped)`,")
                } else if e is [Any?] || e is [String: Any?] {
                    _vString.append("\(_name):\(try convertValue(e)),")
                } else if e == nil || e is NSNull {
                    _vString.append("\(_name):null,")
                } else {
                    throw FlexError.UnuseableTypeCameIn
                }
            }
            _vString.append("}")
            return _vString
        } else if let _value = value as? Encodable {
            return "\(try convertValue(_value.toDictionary))"
        } else {
            throw FlexError.UnuseableTypeCameIn
        }
    }
    
    static func singleArrayToDictionary(_ value: [Any?]?) throws -> [String: Any?]? {
        let _value = value?[0]
        if(_value == nil || _value is NSNull) {
            return nil
        } else if(_value is [String: Any?]) {
            return _value as? [String: Any?]
        } else {
            throw FlexError.UnuseableTypeCameIn
        }
    }
    
    static func arrayToFlexData(_ value: [Any?]?) -> [FlexData] {
        var res: [FlexData] = []
        value?.forEach({ (ele) in
            res.append(anyToFlexData(ele))
        })
        return res
    }
    
    static func dictionaryToFlexData(_ value: [String: Any?]?) -> [String:FlexData] {
        var res: [String:FlexData] = [:]
        value?.keys.forEach({ (key) in
            res[key] = anyToFlexData(value?[key] ?? nil)
        })
        return res
    }
    
    static func anyToFlexData(_ value: Any?) -> FlexData {
        if(value == nil || value is NSNull) {
            return FlexData()
        } else if(value is Int) {
            return FlexData(value as! Int)
        } else if(value is Float) {
            return FlexData(value as! Float)
        } else if(value is Double) {
            return FlexData(value as! Double)
        } else if(value is String) {
            return FlexData(value as! String)
        } else if(value is [Any?]) {
            return FlexData(arrayToFlexData(value as? [Any?]))
        } else if let _value = value as? [String: Any?] {
            if(_value.count == 1 && _value.keys.first == FlexString.CHECKBOOL) {
                if let _chkBool = _value[FlexString.CHECKBOOL] as? Int {
                    if(_chkBool == 0) {
                        return FlexData(false)
                    } else if(_chkBool == 1) {
                        return FlexData(true)
                    } else {
                        return FlexData([:])
                    }
                } else if let _chkBool = _value[FlexString.CHECKBOOL] as? Bool {
                    return FlexData(_chkBool)
                } else {
                    return FlexData(dictionaryToFlexData(_value))
                }
            } else {
                return FlexData(dictionaryToFlexData(_value))
            }
        } else if(value is BrowserException) {
            return FlexData(value as! BrowserException)
        } else {
            FlexMsg.err(FlexString.ERROR8)
            return FlexData()
        }
    }
}
