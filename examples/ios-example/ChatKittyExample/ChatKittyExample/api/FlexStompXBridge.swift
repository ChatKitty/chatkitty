import WebKit
final class FlexStompXBridge: StompXBridge {
    var webView: WKWebView

    init(webView: WKWebView) {
        self.webView = webView
    }
    
    func onMessage<T: Codable>(id: String?,
                               type: FlexStompXEventType,
                               payload: T) {
        

        
        let message = FlexStompXMessage(id: id,
                                    type: type.rawValue,
                                    payload: payload)
        let encoded = message.toJSONString().data(using: .utf8)?.base64EncodedString()
        if encoded != nil {
            if let encoded = encoded {
                webView.evaluateJavaScript("window.$flex.web.onMessage('\(encoded)'); void 0;") {
                    result, error in
                    if let error = error {
                        print("❌ Error calling onMessage: \(error.localizedDescription)")
                    }
                }
            }
        }


    }
    
    func onMessage(id: String?,
                   type: FlexStompXEventType) {
        let message = FlexStompXEmptyMessage(id: id, type: type.rawValue)
        let encoded = message.toJSONString().data(using: .utf8)?.base64EncodedString()
        if encoded != nil {
            if let encoded = encoded {
                webView.evaluateJavaScript("window.$flex.web.onMessage('\(encoded)'); void 0;") {
                    result, error in
                    if let error = error {
                        print("❌ Error calling onMessage: \(error.localizedDescription)")
                    }
                }
            }
        }
    }
    
}
