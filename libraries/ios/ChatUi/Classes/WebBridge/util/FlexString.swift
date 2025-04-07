//
//  FlexString.swift
//  FlexHybridApp
//
//  Created by 황견주 on 2021/11/25.
//  Copyright © 2021 황견주. All rights reserved.
//

import Foundation

struct FlexString {
    static let ERROR1 = "After the FlextWebView is initialized, BaseUrl, Options, Interfaces cannot be added."
    static let ERROR2 = "You cannot set the interface or FlexAction name with flex"
    static let ERROR3 = "Only possible nil, Int, Double, Float, Character, String, Dictionary<String,Any>, Array<Any>, FlexReject, Codable."
    static let ERROR4 = "FlexWebView to run javascript is null."
    static let ERROR5 = "The Interface or Action with the same name is already registered."
    static let ERROR6 = "String included in Baseurl's regular expression rule cannot be set to allowUrl."
    static let ERROR7 = "PromiseReturn cannot be called twice in a single FlexAction."
    static let ERROR8 = "The type of data stored in FlexData and the type called are not identical."
    
    static let FLOG_LOG = "flexlog"
    static let FLOG_ERROR = "flexerror"
    static let FLOG_DEBUG = "flexdebug"
    static let FLOG_INFO = "flexinfo"
    static let FLOG_WARN = "flexwarn"

    static let FLEX_RETURN = "flexreturn"
    static let FLEX_LOAD = "flexload"

    static let EVT_SUC = "flexSuccess"
    static let EVT_EXC = "flexException"
    static let EVT_TINEOUT = "flexTimeout"
    static let EVT_INIT = "flexInit"

    static let FLEX_DEFINE = [FLOG_LOG, FLOG_ERROR, FLOG_DEBUG, FLOG_INFO, FLOG_WARN, FLEX_RETURN, FLEX_LOAD, EVT_SUC, EVT_EXC, EVT_TINEOUT, EVT_INIT]
        
    static let CHECKBOOL = UUID().uuidString
}
