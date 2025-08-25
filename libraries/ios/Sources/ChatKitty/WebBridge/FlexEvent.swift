//
//  FlexEvent.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/02/05.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation

public enum FlexEvent {
    case SUCCESS
    case EXCEPTION
    case TIMEOUT
    case INIT
}


public class FlexListener {
    internal let closure : (_ type: FlexEvent, _ funcName: String, _ url: String, _ msg: String?) -> Void
    internal let id: UUID
    
    init(_ closure: @escaping (_ type: FlexEvent, _ funcName: String, _ url: String, _ msg: String?) -> Void) {
        self.closure = closure
        id = UUID()
    }
}
