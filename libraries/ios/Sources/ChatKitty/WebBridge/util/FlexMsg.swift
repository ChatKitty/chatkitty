//
//  FlexMsg.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/11/25.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation

struct FlexMsg {
    static private let date = DateFormatter()
    static func info(_ info: String) {
        date.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        print("\(date.string(from: Date())) :: FlexWebViewInfo 🔷 : \(info)")
    }
    static func err(_ err: String) {
        date.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        let callStack = Thread.callStackSymbols
        var callStackString = ""
        var index = 0
        callStack.forEach {
            if index != 0 {
                callStackString += "\n" + $0
            }
            index += 1
        }
        print("\(date.string(from: Date())) :: FlexWebViewError 🛑 : \(err)\(callStackString)")
    }
    static func err(_ err: Error) {
        date.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        let callStack = Thread.callStackSymbols
        var callStackString = ""
        var index = 0
        callStack.forEach {
            if index != 0 {
                callStackString += "\n" + $0
            }
            index += 1
        }
        print("\(date.string(from: Date())) :: FlexWebViewError 🛑 : \(err.localizedDescription)\(callStackString)")
    }
    static func webLog(_ type: String, _ msg: [Any?]) {
        date.dateFormat = "yyyy-MM-dd HH:mm:ss.SSS"
        var t = ""
        switch type {
        case FlexString.FLOG_LOG:
            t = "LOG 🟢"
        case FlexString.FLOG_ERROR:
            t = "ERROR 🛑"
        case FlexString.FLOG_WARN:
            t = "WRAN 🟠"
        case FlexString.FLOG_DEBUG:
            t = "DEBUG 🔶"
        case FlexString.FLOG_INFO:
            t = "INFO 🔷"
        default:
            t = type
        }
        if(msg.count == 0) {
            print("\(date.string(from: Date()))  :: FlexWebView CONSOLE-\(t) : ")
            return
        }
        var data: Any? = msg
        if(msg.count == 1) {
            data = msg[0]
        }
        print("\(date.string(from: Date())) :: FlexWebView CONSOLE-\(t) : \(convertValueForLog(data))")
    }
    static func convertValueForLog(_ value: Any?) -> String {
        if value == nil || value is NSNull {
            return "null"
        } else if value is Int || value is Double || value is Float {
            return "\(value!)"
        } else if value is String || value is Character {
            return "\(value!)"
        } else if let _vArray = value as? [Any?] {
            var _vString = "["
            for e in _vArray {
                if e is Int || e is Double || e is Float {
                    _vString.append("\(e!),")
                } else if e is String || e is Character {
                    _vString.append("\(e!),")
                } else if e is [Any?] || e is [String:Any?] {
                    _vString.append("\(convertValueForLog(e)),")
                } else if e == nil || e is NSNull {
                    _vString.append("null")
                } else {
                    return FlexError.UnuseableTypeCameIn.localizedDescription
                }
            }
            _vString.append("]")
            return _vString
        } else if let _vDic = value as? [String:Any?] {
            if _vDic.count == 1 && _vDic.keys.first == FlexString.CHECKBOOL {
                if let _chkBool = _vDic[FlexString.CHECKBOOL] as? Int {
                    if _chkBool == 1 {
                        return "true"
                    } else if _chkBool == 0 {
                        return "false"
                    } else {
                        return "unknown"
                    }
                } else if let _chkBool = _vDic[FlexString.CHECKBOOL] as? Bool {
                    return "\(_chkBool)"
                } else {
                    return "unknown"
                }
            }
            var _vString = "{"
            for (_name, e) in _vDic {
                if e is Int || e is Double || e is Float {
                    _vString.append("\(_name):\(e!),")
                } else if e is String || e is Character {
                    _vString.append("\(_name):\(e!),")
                } else if e is [Any?] || e is [String:Any?] {
                    _vString.append("\(_name):\(convertValueForLog(e)),")
                } else if e == nil || e is NSNull {
                    _vString.append("\(_name):null,")
                } else {
                    return FlexError.UnuseableTypeCameIn.localizedDescription
                }
            }
            _vString.append("}")
            return _vString
        } else {
            return FlexError.UnuseableTypeCameIn.localizedDescription
        }
    }
}
