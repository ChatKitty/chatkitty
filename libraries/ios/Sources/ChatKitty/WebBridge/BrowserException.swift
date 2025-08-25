//
//  BrowserException.swift
//  FlexHybridApp
//
//  Created by dvkyun on 2020/09/01.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation

public class BrowserException {
    let reason: String?
    public init(_ Reason: String?) {
        reason = Reason
    }
    public init() {
        reason = nil
    }
}
