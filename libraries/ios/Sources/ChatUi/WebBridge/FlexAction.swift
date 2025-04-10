//
//  FlexAction.swift
//  FlexHybridApp
//
//  Created by dvkyun on 2020/09/01.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation

public class FlexAction {
    
    private let funcName: String
    private let mComponent: FlexComponent
    private var _isFinished = false
    
    public var isFinished: Bool {
        _isFinished
    }
    public var onFinished: (() -> Void)? = nil
    
    internal init (_ name: String, _ component: FlexComponent) {
        funcName = name
        mComponent = component
    }
    
    @MainActor private func pRetrun(_ response: Any?) {
        if _isFinished {
            FlexMsg.err(FlexString.ERROR7)
            return
        }
        if response is BrowserException {
            let reason = (response as! BrowserException).reason == nil ? "null" : "\"\((response as! BrowserException).reason!)\""
            mComponent.evalJS("$flex.flex.\(funcName)(false, \(reason))")
        } else if response == nil || response is Void {
            mComponent.evalJS("$flex.flex.\(funcName)(true)")
        } else {
            do {
                mComponent.evalJS("$flex.flex.\(funcName)(true, null, \(try FlexFunc.convertValue(response!)))")
            } catch FlexError.UnuseableTypeCameIn {
                FlexMsg.err(FlexString.ERROR3)
                mComponent.evalJS("$flex.flex.\(funcName)(false, \"\(FlexString.ERROR3)\")")
            } catch {
                FlexMsg.err(error)
                mComponent.evalJS("$flex.flex.\(funcName)(false, \"\(error.localizedDescription)\")")
            }
        }
        finish()
    }
    
    @MainActor public func promiseReturn(_ response: Void) {
        pRetrun(response)
    }
       
    @MainActor public func promiseReturn(_ response: String) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Int) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Float) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Double) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Character) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Bool) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Array<Any?>) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Dictionary<String,Any?>) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: BrowserException) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn(_ response: Encodable) {
        pRetrun(response)
    }
    
    @MainActor public func promiseReturn() {
        resolveVoid()
    }
    
    @MainActor public func resolveVoid() {
        if _isFinished {
            FlexMsg.err(FlexString.ERROR7)
            return
        }
        mComponent.evalJS("$flex.flex.\(funcName)(true)")
        finish()
    }
    
    @MainActor public func reject(reason: BrowserException) {
        if _isFinished {
            FlexMsg.err(FlexString.ERROR7)
            return
        }
        _isFinished = true
        let rejectReson = reason.reason == nil ? "null" : "\"\(reason.reason!)\""
        mComponent.evalJS("$flex.flex.\(funcName)(false, \(rejectReson))")
    }
    
    @MainActor public func reject(reason: String) {
        if _isFinished {
            FlexMsg.err(FlexString.ERROR7)
            return
        }
        mComponent.evalJS("$flex.flex.\(funcName)(false, \"\(reason)\")")
        finish()
    }
    
    @MainActor public func reject() {
        if _isFinished {
            FlexMsg.err(FlexString.ERROR7)
            return
        }
        mComponent.evalJS("$flex.flex.\(funcName)(false)")
        finish()
    }
    
    private func finish() {
        _isFinished = true
        onFinished?()
    }
    
}
