//
//  ContentView.swift
//  ChatKittyExample
//
//  Created by Kevin Grafstrom on 2025-11-04.
//

import SwiftUI
import WebKit

struct ContentView: View {
    var body: some View {
        VStack {
            WebView(url: URL(string: "http://10.0.0.54:9002/chat?widget_id=5S7NNZQ9gwANwfYV&username=Kevin%20Grafstrom&environment=development&mode=sandbox")!)
        }
    }
}

struct WebView: UIViewRepresentable {
    let url: URL
    
    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        
        // IMPORTANT: Add the message handler BEFORE creating the script
        configuration.userContentController.add(context.coordinator, name: "logging")
        
        // Inject JavaScript to capture console logs
        let script = """
        (function() {
            // Test if the message handler is available
            if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.logging) {
                window.webkit.messageHandlers.logging.postMessage({type: 'info', message: 'Console logger initialized successfully'});
            }
            
            var originalLog = console.log;
            var originalError = console.error;
            var originalWarn = console.warn;
            var originalInfo = console.info;

            const getMessage = (args) => {
                const message = Array.from(args).map(function(arg) {
                    if (typeof arg === 'object') {
                        try {
                            return JSON.stringify(arg);
                        } catch(e) {
                            return String(arg);
                        }
                    }
                    return String(arg);
                }).join(' ');
                return message
            }

            const postMessage = ({type, message}) => {
                if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.logging) {
                    window.webkit.messageHandlers.logging.postMessage({type: type, message: message});
                }
            }
            
            console.log = function() {
                var message = getMessage(arguments)
                postMessage({type: 'log', message: message})
                originalLog.apply(console, arguments);
            };
            
            console.error = function() {
                var message = getMessage(arguments)
                postMessage({type: 'error', message: message})
                originalError.apply(console, arguments);
            };
            
            console.warn = function() {
                var message = getMessage(arguments)
                postMessage({type: 'warn', message: message})
                originalWarn.apply(console, arguments);
            };
            
            console.info = function() {
                var message = getMessage(arguments)
                postMessage({type: 'info', message: message})
                originalInfo.apply(console, arguments);
            };
        })();
        """
        
        let userScript = WKUserScript(source: script, injectionTime: .atDocumentStart, forMainFrameOnly: false)
        configuration.userContentController.addUserScript(userScript)
        
        // Optional: Enable inline media playback
        configuration.allowsInlineMediaPlayback = true
        
        // Optional: Enable picture-in-picture
        configuration.allowsPictureInPictureMediaPlayback = true
        
        let webView = WKWebView(frame: .zero, configuration: configuration)
        
        // Optional: Set navigation delegate for handling navigation events
        webView.navigationDelegate = context.coordinator
        
        // Enable debugging in Safari (iOS 16.4+)
        if #available(iOS 16.4, *) {
            webView.isInspectable = true
        }
        
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {
        let request = URLRequest(url: url)
        webView.load(request)
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, WKNavigationDelegate, WKScriptMessageHandler {
        let parent: WebView
        
        init(_ parent: WebView) {
            self.parent = parent
        }
        
        // Handle console messages
        func userContentController(_ userContentController: WKUserContentController, didReceive message: WKScriptMessage) {
            
            if message.name == "logging" {
                if let body = message.body as? [String: Any] {
                    let type = body["type"] as? String ?? "log"
                    let logMessage = body["message"] as? String ?? ""
                    
                    let emoji: String
                    switch type {
                    case "error": emoji = "❌"
                    case "warn": emoji = "⚠️"
                    case "info": emoji = "ℹ️"
                    default: emoji = "📝"
                    }
                    
                    print("\(emoji) [WebView \(type.uppercased())]: \(logMessage)")
                } else {
                    print("📱 Received message with unexpected body format: \(message.body)")
                }
            }
        }
        
        // Optional: Handle navigation events
        func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
            print("✅ Page loaded successfully: \(webView.url?.absoluteString ?? "unknown")")
        }
        
        func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
            print("❌ Failed to load page: \(error.localizedDescription)")
        }
        
        func webView(_ webView: WKWebView, didStartProvisionalNavigation navigation: WKNavigation!) {
            print("🔄 Started loading: \(webView.url?.absoluteString ?? "unknown")")
        }
    }
}

#Preview {
    ContentView()
}
