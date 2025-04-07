import UIKit
import WebKit
import AVFoundation
import Photos

public final class ChatUIView: UIView {
    // MARK: Private members
    private let configuration: ChatUIConfiguration
    private let components: ChatUIComponents?
    private let flexComponent = FlexComponent()
    private lazy var bridge: ChatUIBridge = FlexChatUIBridge(component: flexComponent)
    private lazy var stompXBridge: StompXBridge = FlexStompXBridge(component: flexComponent)
    private lazy var chatUiStompXInteractor = ChatUIStompXInteractor(stompX: stompX, stompXBridge: stompXBridge)
    
    // MARK: UI Components
    private lazy var flexWebView: FlexWebView = {
        let view = FlexWebView(frame: .zero, component: flexComponent)
        addSubview(view)
        return view
    }()
    
    // MARK: Initializers
    
    public init(configuration: ChatUIConfiguration,
                components: ChatUIComponents? = nil) {
        self.configuration = configuration
        self.components = components
        super.init(frame: .zero)
        build()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private let stompX = StompXImpl(configuration: StompXConfiguration(isSecure: true,
                                                                       host: "api.chatkitty.com",
                                                                       isDebug: false))
    
    // MARK: Private
    
    private func build() {
        flexWebView.translatesAutoresizingMaskIntoConstraints = false
        if #available(iOS 16.4, *) {
            flexWebView.isInspectable = true
        }
        NSLayoutConstraint.activate([
            flexWebView.topAnchor.constraint(equalTo: safeAreaLayoutGuide.topAnchor),
            flexWebView.leadingAnchor.constraint(equalTo: safeAreaLayoutGuide.leadingAnchor),
            flexWebView.trailingAnchor.constraint(equalTo: safeAreaLayoutGuide.trailingAnchor),
            flexWebView.bottomAnchor.constraint(equalTo: safeAreaLayoutGuide.bottomAnchor)
        ])
        
        let options = InitializeOptions(
            username: configuration.username,
            theme: configuration.theme,
            clientSpecification: ClientSpecification(connection: configuration.connection == nil ? "standalone" : "shared")
        )
        
        bridge.onChatUiConnected { [weak self] in
            self?.bridge.initialize(options: options)
        }
        
        bridge.onChatMounted { [weak self] context in
            self?.components?.onMounted?(context)
        }
        
        bridge.onChatHeaderSelected { [weak self] channel in
            self?.components?.onHeaderSelected?(channel)
        }
        
        bridge.onChatMenuActionSelected { [weak self] action in
            self?.components?.onMenuActionSelected?(action)
        }
        
        bridge.onPostMessage { [weak self] event in
            DispatchQueue.main.async {
                self?.chatUiStompXInteractor.onReceiveMessage(event: event)
            }
        }
        
        flexWebView.load(URLRequest(url: URL(string: "\(kChatUiBaseUtl)/chat?widget_id=\(configuration.widgetId)&environment=production")!))
        
        if let apiKey = configuration.connection?.apiKey {
            stompX.connect(request: StompXConnectRequest(apiKey: apiKey,
                                                         username: configuration.username,
                                                         authParams: nil,
                                                         onConnected: {
            }, onConnectionLost: {
                print("onConnectionLost")
            }, onConnectionResumed: {
                print("onConnectionResumed")
            }, onError: { error in
                print(error.localizedDescription)
            }))
        }
        requestCameraAccess()
    }
}

extension ChatUIView {
    func requestCameraAccess() {
        AVCaptureDevice.requestAccess(for: .video) { granted in
            if granted {
                // Access granted, continue with camera operation
            } else {
                // Handle denied access
            }
        }
    }
}
