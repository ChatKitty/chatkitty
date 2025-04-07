//
//  BeforeFlexEval.swift
//  FlexHybridApp
//
//  Created by dvkyun on 2020/09/01.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation

internal class BeforeFlexEval {
    let name: String
    let sendData: Any?
    let response: ((_ data: FlexData) -> Void)?
    
    init(_ name: String) {
        self.name = name
        sendData = nil
        response = nil
    }
    init(_ name: String, _ sendData: Any) {
        self.name = name
        self.sendData = sendData
        response = nil
    }
    init(_ name: String,_ response:@escaping (FlexData) -> Void) {
        self.name = name
        self.response = response
        sendData = nil
    }
    init(_ name: String, _ sendData: Any, _ response: @escaping (_ data: FlexData) -> Void) {
        self.name = name
        self.sendData = sendData
        self.response = response
    }
}
