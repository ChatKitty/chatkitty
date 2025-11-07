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
            ChatUi(widgetId: "5S7NNZQ9gwANwfYV", username: "kevingrafstrom", mode: "sandbox", apiKey: "2173cddb-5427-4a09-9742-1a1385cb6c46")
        }
    }
}

struct ChatUi: UIViewRepresentable {
    let widgetId: String
    let username: String
    let mode: String
    let apiKey: String
    
    func makeUIView(context: Context) -> WKWebView {
        let configuration = WKWebViewConfiguration()
        
        // IMPORTANT: Add the message handler BEFORE creating the script
        configuration.userContentController.add(context.coordinator, name: "logging")
        configuration.userContentController.add(context.coordinator, name: "flexMessage")
        configuration.userContentController.add(context.coordinator, name: "onChatUiConnected")
        
        // Inject your custom object
        let flexScript = """
        window.$flex = {
            web: {
                initialize: () => {},
                onMessage: () => {}
            },
            callbacks: {},
            _resolveCallback: function(callbackId, result, error) {
                if (window.$flex.callbacks[callbackId]) {
                    const { resolve, reject } = window.$flex.callbacks[callbackId];
                    delete window.$flex.callbacks[callbackId];

                    if (error) {
                        reject(new Error(error));
                    } else {
                        resolve(result);
                    }
                }
            },
            postMessage: function(message) {
                return new Promise((resolve, reject) => {
                    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.flexMessage) {
                        // Generate a unique callback ID
                        const callbackId = 'callback_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9);

                        // Store the resolve/reject functions
                        window.$flex.callbacks[callbackId] = { resolve, reject };

                        // Add callback ID to the message
                        const messageWithCallback = {
                            ...message,
                            _callbackId: callbackId
                        };

                        // Send the message
                        window.webkit.messageHandlers.flexMessage.postMessage(messageWithCallback);

                        // Optional: Add a timeout
                        setTimeout(() => {
                            if (window.$flex.callbacks[callbackId]) {
                                delete window.$flex.callbacks[callbackId];
                                reject(new Error('Message timeout'));
                            }
                        }, 30000); // 30 second timeout
                    } else {
                        reject(new Error('Message handler not available'));
                    }
                });
            },
            onChatUiConnected: function() {
                if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.onChatUiConnected) {
                    window.webkit.messageHandlers.onChatUiConnected.postMessage({});
                }
            },
            onChatMounted: function() {

            }
        };
        """
        
        let flexUserScript = WKUserScript(
            source: flexScript,
            injectionTime: .atDocumentStart, 
            forMainFrameOnly: false
        )
        configuration.userContentController.addUserScript(flexUserScript)
        
        // Inject JavaScript to capture console logs
        let script = """
            (function() {
            // Test if the message handler is available
            if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.logging) {
                window.webkit.messageHandlers.logging.postMessage({type: 'info', message: 'Console logger initialized successfully'});
            }

                // Capture unhandled errors
                window.addEventListener('error', function(event) {
                    var errorMessage = 'Uncaught Error: ' + event.message +
                                     ' at ' + event.filename + ':' + event.lineno + ':' + event.colno;
                    if (event.error && event.error.stack) {
                        errorMessage += '\\n' + event.error.stack;
                    }
                    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.logging) {
                        window.webkit.messageHandlers.logging.postMessage({type: 'error', message: errorMessage});
                    }
                });

                // Capture unhandled promise rejections
                window.addEventListener('unhandledrejection', function(event) {
                    var errorMessage = 'Unhandled Promise Rejection: ' + event.reason;
                    if (event.reason && event.reason.stack) {
                        errorMessage += '\\n' + event.reason.stack;
                    }
                    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.logging) {
                        window.webkit.messageHandlers.logging.postMessage({type: 'error', message: errorMessage});
                    }
                });

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

        // Store the webView reference in the coordinator
        context.coordinator.webView = webView
        let stompXBridge = FlexStompXBridge(webView: webView)
        context.coordinator.stompXBridge = stompXBridge
        // Create and configure StompX instance
        let stompXConfig = StompXConfiguration(
            isSecure: false,
            host: "10.0.0.54:9001",
            isDebug: true
        )

        // CRITICAL: Store strong reference to prevent deallocation
        let stompx = StompXImpl(configuration: stompXConfig)
        context.coordinator.stompX = stompx
        context.coordinator.chatUiStompXInteractor = ChatUIStompXInteractor(stompX: stompx, stompXBridge: stompXBridge)

        // Create the connect request
        let connectRequest = StompXConnectRequest(
            apiKey: apiKey,
            username: username,
            authParams: nil,
            onConnected: {
                print("✅ StompX connected successfully")
            },
            onConnectionLost: {
                print("⚠️ StompX connection lost")
            },
            onConnectionResumed: {
                print("✅ StompX connection resumed")
            },
            onError: { error in
                print("❌ StompX error: \(error.localizedDescription)")
            }
        )

        // Connect to StompX
        stompx.connect(request: connectRequest)

        // Enable debugging in Safari (iOS 16.4+)
        if #available(iOS 16.4, *) {
            webView.isInspectable = true
        }
        
        return webView
    }

    func updateUIView(_ webView: WKWebView, context: Context) {
        guard let url = URL(string: "http://10.0.0.54:9002/chat?widget_id=\(widgetId)&username=\(username)&environment=development&mode=\(mode)") else {
            return
        }
        let request = URLRequest(url: url)
        webView.load(request)
    }

    func makeCoordinator() -> Coordinator {
        Coordinator(self)
    }

    class Coordinator: NSObject, WKNavigationDelegate, WKScriptMessageHandler {
        let parent: ChatUi
        var webView: WKWebView?
        var stompXBridge: StompXBridge?
        var chatUiStompXInteractor: ChatUIStompXInteractor?

        // Add strong reference to keep StompX alive
        var stompX: StompX?
        
        init(_ parent: ChatUi) {
            self.parent = parent
        }

        func resolveFlexPromise(callbackId: String?) {
            // Send success response back to JavaScript
            if let callbackId = callbackId {
                let response = """
                    window.$flex._resolveCallback('\(callbackId)', {success: true, connected: true}, null);
                    """
                self.webView?.evaluateJavaScript(response, completionHandler: nil)
            }
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
            } else if message.name == "flexMessage" {
                if let body = message.body as? [String: Any] {
                    let type = body["type"] as? String ?? "unknown"
                    let callbackId = body["_callbackId"] as? String

                    do {
                        // Convert dictionary to JSON Data
                        let jsonData = try JSONSerialization.data(withJSONObject: body)

                        // Decode to ChatUIMessage
                        let decoder = JSONDecoder()
                        let chatUIMessage = try decoder.decode(ChatUIMessage.self, from: jsonData)

                        // Now you can use chatUIMessage
                        if let chatUiStompXInteractor = self.chatUiStompXInteractor {
                            DispatchQueue.main.async {
                                chatUiStompXInteractor.onReceiveMessage(event: chatUIMessage)
                            }
                        }

                    } catch {
                        print("❌ Failed to decode ChatUIMessage: \(error)")
                    }

                    self.resolveFlexPromise(callbackId: callbackId)
                }
            } else if message.name == "onChatUiConnected" {
                if let body = message.body as? [String: Any] {
                    
                    // Call initialize with parameters
                    let initData = """
                    {
                        "theme": "light",
                        "username": "\(parent.username)",
                        "clientSpecification": {
                            "connection": "shared",
                            "environment": "development",
                            "version": "1.0.0"
                        }
                    }
                    """

                    webView?.evaluateJavaScript("window.$flex.web.initialize(\(initData)); void 0;") { result, error in
                        if let error = error {
                            print("❌ Error calling initialize: \(error.localizedDescription)")
                        } else {
                            print("✅ Successfully called initialize with data")
                        }
                    }
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
