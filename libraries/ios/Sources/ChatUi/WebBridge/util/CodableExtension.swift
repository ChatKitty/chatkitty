//
//  CodableExtension.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/11/25.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation

extension Encodable {
    
    var toDictionary : [String: Any?]? {
        guard let object = try? JSONEncoder().encode(self) else { return nil }
        guard let dictionary = try? JSONSerialization.jsonObject(with: object, options: []) as? [String: Any?] else { return nil }
        return dictionary
    }
    
}

extension Dictionary {
    
    func toObject<T: Decodable>(_ objectType: T.Type) -> T? {
        guard let dictionaries = try? JSONSerialization.data(withJSONObject: self.dictionaryBoolCheck()) else { return nil }
        guard let objects = try? JSONDecoder().decode(objectType, from: dictionaries) else { return nil }
        return objects
    }
    
    private func dictionaryBoolCheck() -> [String: Any?] {
        guard let value = self as? [String: Any?] else {
            return [:]
        }
        var _res: [String: Any?] = [:]
        for (_name, _value) in value {
            if let __value = _value as? [String: Any?] {
                if(__value.count == 1 && __value.keys.first == FlexString.CHECKBOOL) {
                    if let _chkBool = __value[FlexString.CHECKBOOL] as? Int {
                        if(_chkBool == 0) {
                            _res[_name] = false
                        } else if(_chkBool == 1) {
                            _res[_name] = true
                        } else {
                            _res[_name] = [:]
                        }
                    } else if let _chkBool = __value[FlexString.CHECKBOOL] as? Bool {
                        _res[_name] = _chkBool
                    } else {
                        _res[_name] = __value.dictionaryBoolCheck()
                    }
                } else {
                    _res[_name] = __value.dictionaryBoolCheck()
                }
            } else {
                _res[_name] = _value
            }
        }
        return _res
    }
    
}
