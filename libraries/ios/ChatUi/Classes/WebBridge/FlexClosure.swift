//
//  FlexClosure.swift
//  FlexHybridApp
//
//  Created by dvkyun on 2020/10/07.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation

public struct FlexClosure {
    
    static public func interface(_ closure: @escaping (_ arguments: [FlexData]) throws -> Any?) -> (_ arguments: [FlexData]) throws -> Any? {
        return closure
    }
    
    static public func interface<T: Decodable>(_ closure: @escaping (_ arguments: T?) throws -> Any?) -> (_ arguments: T?) throws -> Any? {
        return closure
    }
    
    static public func action(_ closure: @escaping (_ action: FlexAction, _ arguments: [FlexData]) -> Void) -> (_ action: FlexAction, _ arguments: [FlexData]) -> Void {
        return closure
    }
    
    static public func action<T: Decodable>(_ closure: @escaping (_ action: FlexAction, _ model: T?) -> Void) -> (_ action: FlexAction, _ model: T?) -> Void {
        return closure
    }
    
}
