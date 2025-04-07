//
//  FlexWebView.swift
//  flexhybridapp
//
//  Created by dvkyun on 2020/04/13.
//  Copyright © 2020 dvkyun. All rights reserved.
//

import Foundation
import WebKit

fileprivate let VERSION = Bundle(identifier: "app.dvkyun.flexhybridapp")?.infoDictionary?["CFBundleShortVersionString"] as? String ?? "unknown"

@IBDesignable
open class FlexWebView : WKWebView {

    public let component: FlexComponent
    
    public var enableScroll: Bool = true
    
    public var parentViewController: UIViewController? {
        var parentResponder: UIResponder? = self
        while parentResponder != nil {
            parentResponder = parentResponder?.next
            if let viewController = parentResponder as? UIViewController {
                return viewController
            }
        }
        return nil
    }
    
    open override var navigationDelegate: WKNavigationDelegate? {
        didSet {
            component.checkDelegateChange()
        }
    }

    required public init?(coder: NSCoder) {
        component = FlexComponent()
        super.init(coder: coder)
        component.config = configuration
        component.afterWebViewInit(self)
    }
        
    public override init(frame: CGRect, configuration: WKWebViewConfiguration) {
        component = FlexComponent()
        component.config = configuration
        super.init(frame: frame, configuration: component.config)
        component.afterWebViewInit(self)
    }
            
    public init (frame: CGRect, component: FlexComponent) {
        self.component = component
        super.init(frame: frame, configuration: self.component.config)
        self.component.afterWebViewInit(self)
    }
    
    
    public func evalFlexFunc(_ funcName: String) {
        component.evalFlexFunc(funcName)
    }
    
    public func evalFlexFunc(_ funcName: String, _ returnAs: @escaping (_ data: FlexData) -> Void) {
        component.evalFlexFunc(funcName, returnAs)
    }
    
    public func evalFlexFunc(_ funcName: String, sendData: Any) {
        component.evalFlexFunc(funcName, sendData: sendData)
    }
    
    public func evalFlexFunc(_ funcName: String, sendData: Any, _ returnAs: @escaping (_ data: FlexData) -> Void) {
        component.evalFlexFunc(funcName, sendData: sendData, returnAs)
    }
    
}


open class FlexComponent: NSObject, WKNavigationDelegate, WKScriptMessageHandler, UIScrollViewDelegate {
   
    private var interfaces: [String:(_ arguments: [FlexData]) throws -> Any?] = [:]
    private var typeInterfaces: [String:(_ argument: [String: Any?]) throws -> Any?] = [:]
    private var actions: [String: (_ action: FlexAction, _ arguments: [FlexData]) -> Void] = [:]
    private var typeActions: [String: (_ action: FlexAction, _ argument: [String: Any?]) -> Void] = [:]
    private var iTimeouts: [String: Int] = [:]
    
    private var options: [String: Any] = [:]
    
    private var dependencies: [String] = []
    
    private var returnFromWeb: [Int:(_ data: FlexData) -> Void] = [:]
    
    private var flexWebView: FlexWebView? = nil
    
    private var jsString: String? = nil
    private var baseUrl: String? = nil
    private var allowUrlMap: [String: Bool] = [:]
    private var recentConfigRuleString: String? = nil
    private var applyContentRuleList: Any? = nil
    
    private var showWebViewConsole = true
    
    private var userNavigation: WKNavigationDelegate? = nil
    
    private let queue = DispatchQueue(label: "FlexibleHybridApp", qos: DispatchQoS.background, attributes: .concurrent)
    
    internal var config: WKWebViewConfiguration = WKWebViewConfiguration()
    
    private var beforeFlexLoadEvalList : [BeforeFlexEval] = []
    
    private var isFlexLoad = false
    private var isFirstPageLoad = false
    
    private var isAutoCookieManage = false;
    private var cookieDefaultsName = "FlexCookieManage"
    private var cookieKeys = UUID().uuidString
    
    private var flexEventList : Array<(FlexEvent, FlexListener)> = []
        
    public func addEventListener(_ type: FlexEvent, _ listener: FlexListener) {
        flexEventList.append((type, listener))
    }
    
    public func addEventListener(_ type: FlexEvent, _ closure: @escaping (_ type: FlexEvent, _ funcName: String, _ url: String, _ msg: String?) -> Void) {
        flexEventList.append((type, FlexListener(closure)))
    }
    
    public func addEventListener(_ listener: FlexListener) {
        flexEventList.append((FlexEvent.SUCCESS, listener))
        flexEventList.append((FlexEvent.EXCEPTION, listener))
        flexEventList.append((FlexEvent.TIMEOUT, listener))
        flexEventList.append((FlexEvent.INIT, listener))
    }
    
    public func addEventListener(_ closure: @escaping (_ type: FlexEvent, _ funcName: String, _ url: String, _ msg: String?) -> Void) {
        addEventListener(FlexListener(closure))
    }
    
    public func removeEventListener(listener: FlexListener) {
        flexEventList = flexEventList.filter { (tuple) -> Bool in
            if tuple.1.id == listener.id {
                return false
            }
            return true
        }
    }
    
    public func removeAllEventListener() {
        flexEventList = []
    }
    
    @available(iOS 11.0 , *)
    public func setAutoCookieManage(_ isAutoManage: Bool, clearAll : Bool = false) {
        if clearAll {
            removeAllCookieInManage()
        }
        isAutoCookieManage = isAutoManage
    }
    
    @available(iOS 11.0 , *)
    public func getAutoCookieManage() -> Bool {
        return isAutoCookieManage
    }
    
    public var BaseUrl: String? {
        baseUrl
    }
    
    public var webView: FlexWebView? {
        flexWebView
    }
       
    public var configuration: WKWebViewConfiguration {
        config
    }
    
    public var ShowWebViewConsole: Bool {
        showWebViewConsole
    }
    
    public func setBaseUrl(_ url: String) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else {
            baseUrl = url
        }
    }
    
    @available(iOS 11.0, *)
    public func addAllowUrl(_ urlString: String, canFlexLoad: Bool = false) {
        if let baseUrl = baseUrl, urlString.range(of: baseUrl, options: .regularExpression) != nil {
            FlexMsg.err(FlexString.ERROR6)
            return
        }
        allowUrlMap[urlString] = canFlexLoad
        configAllowContentRule()
    }
    
    @available(iOS 11.0, *)
    public func removeAllowUrl(_ urlString: String) {
        allowUrlMap.removeValue(forKey: urlString)
        if allowUrlMap.count == 0 {
            if let _applyContentRuleList = applyContentRuleList as? WKContentRuleList {
                config.userContentController.remove(_applyContentRuleList)
            }
        } else {
            configAllowContentRule()
        }
    }
    
    @available(iOS 11.0, *)
    private func configAllowContentRule() {
        var rule = [["trigger":["url-filter":".*", "resource-type":["document"]],"action":["type":"block"]]]
        if let _baseUrl = baseUrl {
            rule.append(["trigger":["url-filter":"\(_baseUrl)"],"action":["type":"ignore-previous-rules"]])
        }
        for _urlString in allowUrlMap.keys {
            rule.append(["trigger":["url-filter":"\(_urlString)"],"action":["type":"ignore-previous-rules"]])
        }
        
        guard let jsonData = try? JSONSerialization.data(withJSONObject: rule, options: []) else {
            return
        }
        let ruleString = String(data: jsonData, encoding: String.Encoding.utf8)
        
        recentConfigRuleString = ruleString
        
        WKContentRuleListStore.default()?.compileContentRuleList(
            forIdentifier: "ContentRuleList",
            encodedContentRuleList: ruleString
        ) { (contentRuleList, error) in
            if self.recentConfigRuleString != ruleString {
                return
            }
            
            if error != nil {
                return
            }
            guard let contentRuleList = contentRuleList else {
                return
            }
                        
            let configuration = self.config
            
            if let _applyContentRuleList = self.applyContentRuleList as? WKContentRuleList {
                configuration.userContentController.remove(_applyContentRuleList)
            }
            configuration.userContentController.add(contentRuleList)
            self.applyContentRuleList = contentRuleList
        }
    }
    
    public func setShowWebViewConsole(_ enable: Bool) {
        showWebViewConsole = enable
    }
    
    public func setInterfaceTimeout(_ timeout: Int) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else {
            options["timeout"] = timeout
        }
    }
    
    public func setFlexOnloadWait(_ time: Int) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else {
            options["flexLoadWait"] = time
        }
    }
    
    public func setDependency(_ js: String) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else {
            dependencies.append(js)
        }
    }
    
    public func setAllowsUrlAccessInFile(_ allow: Bool) {
        config.preferences.setValue(allow, forKey: "allowFileAccessFromFileURLs")
        config.setValue(allow, forKey: "allowUniversalAccessFromFileURLs")
    }

    
    private func flexInterfaceInit() {
        if !isFirstPageLoad {
            do {
                let url = Bundle.module.url(forResource: "FlexHybridiOS", withExtension: "js")
                jsString = try String(contentsOf: url!, encoding: .utf8)
                var keys = ""
                keys.append("[\"")
                keys.append(FlexString.FLEX_DEFINE.joined(separator: "\",\""))
                if(interfaces.count > 0) {
                    keys.append("\",\"")
                    keys.append(interfaces.keys.joined(separator: "\",\""))
                }
                if(typeInterfaces.count > 0) {
                    keys.append("\",\"")
                    keys.append(typeInterfaces.keys.joined(separator: "\",\""))
                }
                if(actions.count > 0) {
                    keys.append("\",\"")
                    keys.append(actions.keys.joined(separator: "\",\""))
                }
                if(typeActions.count > 0) {
                    keys.append("\",\"")
                    keys.append(typeActions.keys.joined(separator: "\",\""))
                }
                keys.append("\"]")
                jsString = jsString?.replacingOccurrences(of: "versionFromiOS", with: "'\(VERSION)'")
                jsString = jsString?.replacingOccurrences(of: "keysfromios", with: keys)
                jsString = jsString?.replacingOccurrences(of: "defineflexfromios", with: try FlexFunc.convertValue(FlexString.FLEX_DEFINE))
                jsString = jsString?.replacingOccurrences(of: "timesfromios", with: try FlexFunc.convertValue(iTimeouts))
                jsString = jsString?.replacingOccurrences(of: "optionsfromios", with: try FlexFunc.convertValue(options))
                jsString = jsString?.replacingOccurrences(of: "deviceinfofromios", with: try FlexFunc.convertValue(DeviceInfo.getInfo()))
                jsString = jsString?.replacingOccurrences(of: "checkboolfromios", with: "'\(FlexString.CHECKBOOL)'")
                for n in FlexString.FLEX_DEFINE {
                    config.userContentController.add(self, name: n)
                }
                for (n, _) in interfaces {
                    config.userContentController.add(self, name: n)
                }
                for (n, _) in typeInterfaces {
                    config.userContentController.add(self, name: n)
                }
                for (n, _) in actions {
                    config.userContentController.add(self, name: n)
                }
                for (n, _) in typeActions {
                    config.userContentController.add(self, name: n)
                }
                isFirstPageLoad = true
            } catch {
                FlexMsg.err(error)
            }
        }
    }
    
    internal func afterWebViewInit(_ webView: FlexWebView) {
        flexWebView = webView
        flexWebView?.scrollView.delegate = self
        checkDelegateChange()
    }
    
    internal func checkDelegateChange() {
        if flexWebView?.navigationDelegate != nil {
            if !(flexWebView?.navigationDelegate!.isEqual(self) ?? false) {
                userNavigation = flexWebView?.navigationDelegate
                flexWebView?.navigationDelegate = self
            }
        } else {
            flexWebView?.navigationDelegate = self
        }
    }
    
    public func evalJS(_ js: String) {
        DispatchQueue.main.async {
            if self.flexWebView == nil {
                FlexMsg.err(FlexString.ERROR4)
                return
            }
            self.flexWebView?.evaluateJavaScript(js + ";void 0;", completionHandler: { _, Error in
                if Error != nil {
                    FlexMsg.info("EvaluateJS fail - \(Error?.localizedDescription ?? "")")
                }
            })
        }
    }
                
    public func setInterface(_ name: String, _ timeout: Int? = nil, _ interface: @escaping (_ arguments: [FlexData]) throws -> Any?) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else if interfaces[name] != nil || actions[name] != nil {
            FlexMsg.err(FlexString.ERROR5)
        } else if name.contains("flex") {
            FlexMsg.err(FlexString.ERROR2)
        } else {
            if timeout != nil {
                iTimeouts[name] = timeout
            }
            interfaces[name] = interface
        }
    }
    
    public func setInterface(_ name: String, _ interface: @escaping (_ arguments: [FlexData]) throws -> Any?) {
        setInterface(name, nil, interface)
    }
    
    public func setInterface<T: Decodable>(_ name: String, _ timeout: Int? = nil, _ interface: @escaping (_ model: T?) throws -> Any?) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else if interfaces[name] != nil || actions[name] != nil {
            FlexMsg.err(FlexString.ERROR5)
        } else if name.contains("flex") {
            FlexMsg.err(FlexString.ERROR2)
        } else {
            if timeout != nil {
                iTimeouts[name] = timeout
            }
            typeInterfaces[name] = { dic in
                try interface(dic.toObject(T.self))
            }
        }
    }
    
    public func setInterface<T: Decodable>(_ name: String, _ interface: @escaping (_ model: T?) throws -> Any?) {
        setInterface(name, nil, interface)
    }
    
    public func setAction(_ name: String, _ timeout: Int? = nil, _ action: @escaping (_ action: FlexAction, _ arguments: [FlexData]) -> Void) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else if interfaces[name] != nil || actions[name] != nil {
            FlexMsg.err(FlexString.ERROR5)
        } else if name.contains("flex") {
            FlexMsg.err(FlexString.ERROR2)
        } else {
            if timeout != nil {
                iTimeouts[name] = timeout
            }
            actions[name] = action
        }
    }
    
    public func setAction(_ name: String, _ action: @escaping (_ action: FlexAction, _ arguments: [FlexData]) -> Void) {
        setAction(name, nil, action)
    }
    
    public func setAction<T: Decodable>(_ name: String, _ timeout: Int? = nil, _ action: @escaping (_ action: FlexAction, _ model: T?) -> Void) {
        if isFirstPageLoad {
            FlexMsg.err(FlexString.ERROR1)
        } else if interfaces[name] != nil || actions[name] != nil {
            FlexMsg.err(FlexString.ERROR5)
        } else if name.contains("flex") {
            FlexMsg.err(FlexString.ERROR2)
        } else {
            if timeout != nil {
                iTimeouts[name] = timeout
            }
            typeActions[name] = { ac, dic in
                action(ac, dic.toObject(T.self))
            }
        }
    }
    
    public func setAction<T: Decodable>(_ name: String, _ action: @escaping (_ action: FlexAction, _ model: T?) -> Void) {
        setAction(name, nil, action)
    }
        
    public func evalFlexFunc(_ funcName: String) {
        if(!isFlexLoad) {
            beforeFlexLoadEvalList.append(BeforeFlexEval(funcName))
        } else {
            evalJS("!function() {try {const a = $flex.web.\(funcName)();if (a instanceof Promise) {a.then(function(a) {$flex.flexSuccess('web.\(funcName)', location.href, a);}).catch(function(a) {$flex.flexException('web.\(funcName)', location.href, a.toString());})} else {$flex.flexSuccess('web.\(funcName)', location.href, a);}} catch (a) {$flex.flexException('web.\(funcName)', location.href, a.toString());}}();")
        }
    }
    
    public func evalFlexFunc(_ funcName: String, _ returnAs: @escaping (_ data: FlexData) -> Void) {
        if(!isFlexLoad) {
            beforeFlexLoadEvalList.append(BeforeFlexEval(funcName, returnAs))
        } else {
            let TID = Int.random(in: 1..<10000)
            returnFromWeb[TID] = returnAs
            evalJS("!function() {try {const a = $flex.web.\(funcName)();if (a instanceof Promise) {a.then(function(a) {$flex.flexSuccess('web.\(funcName)', location.href, a);$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:0});}).catch(function(a) {$flex.flexException('web.\(funcName)', location.href, a.toString());$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:1});})} else {$flex.flexSuccess('web.\(funcName)', location.href, a);$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:0});}} catch (a) {$flex.flexException('web.\(funcName)', location.href, a.toString());$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:1});}}();")
        }
    }
    
    public func evalFlexFunc(_ funcName: String, sendData: Any) {
        if(!isFlexLoad) {
            beforeFlexLoadEvalList.append(BeforeFlexEval(funcName, sendData))
        } else {
            do {
                evalJS("!function() {try {const a = $flex.web.\(funcName)(\(try FlexFunc.convertValue(sendData)));if (a instanceof Promise) {a.then(function(a) {$flex.flexSuccess('web.\(funcName)', location.href, a);}).catch(function(a) {$flex.flexException('web.\(funcName)', location.href, a.toString());})} else {$flex.flexSuccess('web.\(funcName)', location.href, a);}} catch (a) {$flex.flexException('web.\(funcName)', location.href, a.toString());}}();")
            } catch FlexError.UnuseableTypeCameIn {
                FlexMsg.err(FlexString.ERROR3)
            } catch {
                FlexMsg.err(error)
            }
        }
    }
    
    public func evalFlexFunc(_ funcName: String, sendData: Any, _ returnAs: @escaping (_ data: FlexData) -> Void) {
        if(!isFlexLoad) {
            beforeFlexLoadEvalList.append(BeforeFlexEval(funcName, sendData, returnAs))
        } else {
            do {
                let TID = Int.random(in: 1..<10000)
                returnFromWeb[TID] = returnAs
                evalJS("!function() {try {const a = $flex.web.\(funcName)(\(try FlexFunc.convertValue(sendData)));if (a instanceof Promise) {a.then(function(a) {$flex.flexSuccess('web.\(funcName)', location.href, a);$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:0});}).catch(function(a) {$flex.flexException('web.\(funcName)', location.href, a.toString());$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:1});})} else {$flex.flexSuccess('web.\(funcName)', location.href, a);$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:0});}} catch (a) {$flex.flexException('web.\(funcName)', location.href, a.toString());$flex.flexreturn({Name:'\(funcName)',Url:location.href,TID:\(TID),Value:a,Error:1});}}();")
            } catch FlexError.UnuseableTypeCameIn {
                FlexMsg.err(FlexString.ERROR3)
            } catch {
                FlexMsg.err(error)
            }
        }
    }
    
    public var parentViewController: UIViewController? {
        var parentResponder: UIResponder? = flexWebView
        while parentResponder != nil {
            parentResponder = parentResponder?.next
            if let viewController = parentResponder as? UIViewController {
                return viewController
            }
        }
        return nil
    }
                                
    public func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
        if let data: [String:Any] = message.body as? Dictionary {
            let mName = message.name
            let fName = data["funName"] as! String
            if FlexString.FLEX_DEFINE.contains(mName) {
                // framework inner interface
                queue.async {
                    switch(mName) {
                    // WebLogs
                    case FlexString.FLOG_LOG, FlexString.FLOG_INFO, FlexString.FLOG_DEBUG, FlexString.FLOG_ERROR, FlexString.FLOG_WARN:
                        if self.showWebViewConsole {
                            FlexMsg.webLog(mName, data["arguments"] as! [Any?])
                        }
                        self.evalJS("$flex.flex.\(fName)(true)")
                    // $flex.web func return
                    case FlexString.FLEX_RETURN:
                        let webData = data["arguments"] as! Array<Dictionary<String, Any?>>
                        let iData = webData[0]
                        let TID = (iData["TID"] as? Int) ?? 0
                        _ = iData["Url"] as! String
                        _ = iData["Name"] as! String
                        let value = iData["Value"] as Any?
                        let error = iData["Error"] as! Int
                        if(error == 1) {
                            var errMsg: String? = nil
                            if(value is String) {
                                errMsg = value as? String
                            }
                            self.returnFromWeb[TID]?(FlexFunc.anyToFlexData(BrowserException(errMsg)))
                        } else {
                            self.returnFromWeb[TID]?(FlexFunc.anyToFlexData(value))
                        }
                        self.returnFromWeb[TID] = nil
                        self.evalJS("$flex.flex.\(fName)(true)")
                    // flexload
                    case FlexString.FLEX_LOAD:
                        if self.isFlexLoad {
                            self.evalJS("$flex.flex.\(fName)(true)")
                            break
                        }
                        self.isFlexLoad = true
                        self.beforeFlexLoadEvalList.forEach { item in
                            if(item.sendData != nil && item.response != nil) {
                                self.evalFlexFunc(item.name, sendData: item.sendData!, item.response!)
                            } else if(item.sendData != nil && item.response == nil) {
                                self.evalFlexFunc(item.name, sendData: item.sendData!)
                            } else if(item.sendData == nil && item.response != nil) {
                                self.evalFlexFunc(item.name, item.response!)
                            } else {
                                self.evalFlexFunc(item.name)
                            }
                        }
                        self.beforeFlexLoadEvalList.removeAll()
                        self.evalJS("$flex.flex.\(fName)(true)")
                    // events
                    case FlexString.EVT_EXC, FlexString.EVT_SUC, FlexString.EVT_INIT, FlexString.EVT_TINEOUT:
                        self.evalJS("$flex.flex.\(fName)(true)")
                        guard let args = data["arguments"] as? [Any?] else { return }
                        guard let funcName = args[0] as? String else { return }
                        guard let url = args[1] as? String else { return }
                        var msg: String? = nil
                        if args.count > 2 {
                            msg = FlexFunc.anyToFlexData(args[2]).toString()
                        }
                        let type : FlexEvent
                        switch mName {
                        case FlexString.EVT_SUC:
                            type = FlexEvent.SUCCESS
                        case FlexString.EVT_EXC:
                            type = FlexEvent.EXCEPTION
                        case FlexString.EVT_TINEOUT:
                            type = FlexEvent.TIMEOUT
                        case FlexString.EVT_INIT:
                            type = FlexEvent.INIT
                        default:
                            type = FlexEvent.EXCEPTION
                        }
                        self.flexEventList.forEach { (tuple) in
                            if tuple.0 == type {
                                tuple.1.closure(type, funcName, url, msg)
                            }
                        }
                    default:
                        break
                    }
                }
            } else if let _closure = interfaces[mName] {
                // user interface
                queue.async {
                    do {
                        let value: Any? = try _closure(FlexFunc.arrayToFlexData(data["arguments"] as? [Any?]))
                        if value is BrowserException {
                            let reason = (value as! BrowserException).reason == nil ? "null" : "\"\((value as! BrowserException).reason!)\""
                            self.evalJS("$flex.flex.\(fName)(false, \(reason))")
                        } else if value == nil || value is Void {
                            self.evalJS("$flex.flex.\(fName)(true)")
                        } else {
                            self.evalJS("$flex.flex.\(fName)(true, null, \(try FlexFunc.convertValue(value!)))")
                        }
                    } catch FlexError.UnuseableTypeCameIn {
                        FlexMsg.err(FlexString.ERROR3)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(FlexString.ERROR3)\")")
                    } catch {
                        FlexMsg.err(error)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(error.localizedDescription)\")")
                    }
                }
            } else if let _closure = typeInterfaces[mName] {
                // user interface
                queue.async {
                    do {
                        let value: Any? = try _closure(FlexFunc.singleArrayToDictionary(data["arguments"] as? [Any?]) ?? [:])
                        if value is BrowserException {
                            let reason = (value as! BrowserException).reason == nil ? "null" : "\"\((value as! BrowserException).reason!)\""
                            self.evalJS("$flex.flex.\(fName)(false, \(reason))")
                        } else if value == nil || value is Void {
                            self.evalJS("$flex.flex.\(fName)(true)")
                        } else {
                            self.evalJS("$flex.flex.\(fName)(true, null, \(try FlexFunc.convertValue(value!)))")
                        }
                    } catch FlexError.UnuseableTypeCameIn {
                        FlexMsg.err(FlexString.ERROR3)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(FlexString.ERROR3)\")")
                    } catch {
                        FlexMsg.err(error)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(error.localizedDescription)\")")
                    }
                }
            } else if let _action = actions[mName] {
                // user action interface
                queue.async {
                    _action(FlexAction(fName, self), FlexFunc.arrayToFlexData(data["arguments"] as? [Any?]))
                }
            } else if let _action = typeActions[mName] {
                queue.async {
                    do {
                        _action(FlexAction(fName, self), try FlexFunc.singleArrayToDictionary(data["arguments"] as? [Any?]) ?? [:])
                    } catch FlexError.UnuseableTypeCameIn {
                        FlexMsg.err(FlexString.ERROR3)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(FlexString.ERROR3)\")")
                    } catch {
                        FlexMsg.err(error)
                        self.evalJS("$flex.flex.\(fName)(false, \"\(error.localizedDescription)\")")
                    }
                }
            }
        }
    }
    
    /*
     WKNavigationDelegate
     */
    public func webView(_ webView: WKWebView, didCommit navigation: WKNavigation!) {
        if let url = webView.url {
            var needFlexLoad = false
            if let baseUrl = baseUrl, url.absoluteString.range(of: baseUrl, options: .regularExpression) != nil {
                needFlexLoad = true
            } else {
                for (pattern, canLoadFlex) in allowUrlMap {
                    if url.absoluteString.range(of: pattern, options: .regularExpression) != nil {
                        needFlexLoad = canLoadFlex
                        break
                    }
                }
            }
            if needFlexLoad {
                isFlexLoad = false
                flexInterfaceInit()
                evalJS(jsString ?? "")
                dependencies.forEach { (js) in
                    evalJS(js)
                }
                evalJS("window.$FCheck = true;")
            }
        }
        userNavigation?.webView?(webView, didCommit: navigation)
    }
    
    public func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        if #available(iOS 11.0, *), isAutoCookieManage {
            saveCookie()
        }
        if let url = webView.url {
            var needFlexLoad = false
            if baseUrl == nil || (baseUrl != nil && url.absoluteString.contains(baseUrl!)) {
                needFlexLoad = true
            }
            for (pattern, canLoadFlex) in allowUrlMap {
                if url.absoluteString.range(of: pattern, options: .regularExpression) != nil {
                    needFlexLoad = canLoadFlex
                    break
                }
            }
            if needFlexLoad {
                evalJS("if(typeof window.$flex === 'undefined' || window.$flex.isScript) { \(jsString ?? "") }")
                evalJS("setTimeout(()=>{const evalFrames=e=>{for(let o=0;o<e.frames.length;o++){if(void 0===e.frames[o].$flex){Object.defineProperty(e.frames[o],\"$flex\",{value:window.$flex,writable:!1,enumerable:!0});let n=void 0;\"function\"==typeof e.frames[o].onFlexLoad&&(n=e.frames[o].onFlexLoad),Object.defineProperty(e.frames[o],\"onFlexLoad\",{set:function(e){window.onFlexLoad=e},get:function(){return window._onFlexLoad}}),\"function\"==typeof n&&(e.frames[o].onFlexLoad=n)}evalFrames(e.frames[o])}};evalFrames(window);},0);")
                dependencies.forEach { (js) in
                    evalJS("if(typeof window.$FCheck === 'undefined') { \(js) }")
                }
            }
        }
        userNavigation?.webView?(webView, didFinish: navigation)
    }
    
    public func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
        if #available(iOS 11.0, *), isAutoCookieManage {
            loadCookie()
        }
        userNavigation?.webView?(webView, didStartProvisionalNavigation: navigation)
    }
    
    public func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        FlexMsg.err(error.localizedDescription)
        userNavigation?.webView?(webView, didFail: navigation, withError: error)
    }
    
    public func webView(_ webView: WKWebView, didReceiveServerRedirectForProvisionalNavigation navigation: WKNavigation!) {
        userNavigation?.webView?(webView, didReceiveServerRedirectForProvisionalNavigation: navigation)
    }
    
    public func webView(_ webView: WKWebView, didFailProvisionalNavigation navigation: WKNavigation!, withError error: Error) {
        userNavigation?.webView?(webView, didFailProvisionalNavigation: navigation, withError: error)
    }
    
    public func webView(_ webView: WKWebView, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        (userNavigation?.webView ?? inWeb)(webView, challenge, completionHandler)
    }
    
    public func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        (userNavigation?.webView ?? inWeb)(webView, navigationAction, decisionHandler)
    }
            
    public func webView(_ webView: WKWebView, decidePolicyFor navigationResponse: WKNavigationResponse, decisionHandler: @escaping (WKNavigationResponsePolicy) -> Void) {
        if #available(iOS 11.0, *), isAutoCookieManage {
            saveCookie()
        }
        if (navigationResponse.response is HTTPURLResponse) {
            let response = navigationResponse.response as? HTTPURLResponse
            FlexMsg.info(String(format: "response.statusCode: %ld", response?.statusCode ?? 0))
        }
        (userNavigation?.webView ?? inWeb)(webView, navigationResponse, decisionHandler)
    }
    
    @available(iOS 13.0, *)
    public func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, preferences: WKWebpagePreferences, decisionHandler: @escaping (WKNavigationActionPolicy, WKWebpagePreferences) -> Void) {
        if isAutoCookieManage {
            saveCookie()
        }
        (userNavigation?.webView ?? inWeb)(webView, navigationAction, preferences, decisionHandler)
    }
    
    private func inWeb(_ webView: WKWebView, didReceive challenge: URLAuthenticationChallenge, completionHandler: @escaping (URLSession.AuthChallengeDisposition, URLCredential?) -> Void) {
        completionHandler(URLSession.AuthChallengeDisposition.performDefaultHandling, nil)
    }
    
    private func inWeb(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
        if (navigationAction.request.url?.absoluteString.hasPrefix("http:"))! || (navigationAction.request.url?.absoluteString.hasPrefix("https:"))! || (navigationAction.request.url?.absoluteString.hasPrefix("file:"))! {
            decisionHandler(.allow)
        } else {
            if let aString = URL(string: (navigationAction.request.url?.absoluteString)!) {
                UIApplication.shared.open(aString, options: [:], completionHandler: {success in
                    if success {
                        FlexMsg.info("Opend \(navigationAction.request.url?.absoluteString ?? "")")
                    } else {
                        FlexMsg.err("Failed \(navigationAction.request.url?.absoluteString ?? "")")
                    }
                })
            }
            decisionHandler(.cancel)
        }
    }
    
    private func inWeb(_ webView: WKWebView, decidePolicyFor navigationResponse: WKNavigationResponse, decisionHandler: @escaping (WKNavigationResponsePolicy) -> Void) {
        decisionHandler(.allow)
    }
    
        
    @available(iOS 13.0, *)
    private func inWeb(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction, preferences: WKWebpagePreferences, decisionHandler: @escaping (WKNavigationActionPolicy, WKWebpagePreferences) -> Void) {
        if (navigationAction.request.url?.absoluteString.hasPrefix("http:"))! || (navigationAction.request.url?.absoluteString.hasPrefix("https:"))! || (navigationAction.request.url?.absoluteString.hasPrefix("file:"))! {
            decisionHandler(.allow, preferences)
        } else {
            if let aString = URL(string: (navigationAction.request.url?.absoluteString)!) {
                UIApplication.shared.open(aString, options: [:], completionHandler: {success in
                    if success {
                        FlexMsg.info("Opend \(navigationAction.request.url?.absoluteString ?? "")")
                    } else {
                        FlexMsg.err("Failed \(navigationAction.request.url?.absoluteString ?? "")")
                    }
                })
            }
            decisionHandler(.cancel, preferences)
        }
    }
    
    /*
    UIScrollViewDelegate
    */
    public func scrollViewDidScroll(_ scrollView: UIScrollView) {
        if !flexWebView!.enableScroll {
            scrollView.setContentOffset(CGPoint(x: 0, y: 0), animated: false)
        }
    }
    
    
    /*
     AutoCookieManage Functions
     */
    @available(iOS 11.0 , *)
    private func saveCookie() {
        guard let cookieDefaults = UserDefaults.init(suiteName: cookieDefaultsName) else {
            return
        }
        guard var keys : Array<String> = cookieDefaults.stringArray(forKey: cookieKeys) else {
            return
        }
        config.websiteDataStore.httpCookieStore.getAllCookies { (cookies) in
            cookies.forEach { (cookie) in
                var values: Array<String> = []
                let formatter = DateFormatter()
                let key = "\(cookie.domain):\(cookie.name)"
                formatter.dateFormat = "EEE',' dd' 'MMM' 'yyyy HH':'mm':'ss zzz"
                values[0] = cookie.name
                values[1] = cookie.value
                values[2] = cookie.domain
                values[3] = cookie.expiresDate != nil ? formatter.string(from: cookie.expiresDate!) : ""
                values[4] = cookie.path
                cookieDefaults.setValue(values, forKey: key)
                if !keys.contains(key) {
                    keys.append(key)
                }
            }
            keys = keys.filter { (copiedKey) -> Bool in
                var isFindDomain = false
                var isFindKey = false
                let domain = copiedKey.split(separator: ":")[0]
                for cookie in cookies {
                    let keyInCookie = "\(cookie.domain):\(cookie.name)"
                    if(domain == cookie.domain) {
                        isFindDomain = true
                    }
                    if(keyInCookie == copiedKey) {
                        isFindKey = true
                    }
                    if(isFindDomain && !isFindKey) {
                        cookieDefaults.removeObject(forKey: keyInCookie)
                        return false
                    }
                }
                return true
            }
//            let keyCopy : Array<String> = Array.init(keys)
//            keyCopy.forEach { (copiedKey) in
//                var isFindDomain = false
//                var isFindKey = false
//                let domain = copiedKey.split(separator: ":")[0]
//                cookies.forEach { (cookie) in
//                    let keyInCookie = "\(cookie.domain):\(cookie.name)"
//                    if(domain == cookie.domain) {
//                        isFindDomain = true
//                    }
//                    if(keyInCookie == copiedKey) {
//                        isFindKey = true
//                    }
//                    if(isFindDomain && !isFindKey) {
//                        guard let keyIndex = keys.firstIndex(of: keyInCookie) else {
//                            return
//                        }
//                        keys.remove(at: keyIndex)
//                        cookieDefaults.removeObject(forKey: keyInCookie)
//                    }
//                }
//            }
            cookieDefaults.setValue(keys, forKey: self.cookieKeys)
        }
    }
    
    @available(iOS 11.0, *)
    private func loadCookie() {
        guard let cookieDefaults = UserDefaults.init(suiteName: cookieDefaultsName) else {
            return
        }
        guard let keys : Array<String> = cookieDefaults.stringArray(forKey: cookieKeys) else {
            return
        }
        keys.forEach { (key) in
            guard let values = cookieDefaults.stringArray(forKey: key) else {
                return
            }
            let wkCookieStore = config.websiteDataStore.httpCookieStore
            var properties : Dictionary<HTTPCookiePropertyKey, Any> = [:]
            properties[HTTPCookiePropertyKey.name] = values[0]
            properties[HTTPCookiePropertyKey.value] = values[1]
            properties[HTTPCookiePropertyKey.domain] = values[2]
            properties[HTTPCookiePropertyKey.expires] = values[3]
            properties[HTTPCookiePropertyKey.path] = values[4]
            guard let completeCookie = HTTPCookie(properties: properties) else {
                return
            }
            wkCookieStore.setCookie(completeCookie, completionHandler: nil)
        }
    }
    
    @available(iOS 11.0, *)
    private func removeAllCookieInManage() {
        guard let cookieDefaults = UserDefaults.init(suiteName: cookieDefaultsName) else {
            return
        }
        guard let keys : Array<String> = cookieDefaults.stringArray(forKey: cookieKeys) else {
            return
        }
        keys.forEach { (key) in
            cookieDefaults.removeObject(forKey: key)
        }
        cookieDefaults.removeObject(forKey: cookieKeys)
    }
    
}
