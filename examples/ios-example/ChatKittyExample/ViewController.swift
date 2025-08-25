import UIKit
import ChatKitty

class ViewController: UIViewController {
    private lazy var chatUi: ChatUIView = {
        let connection = ApiConnection(apiKey: "afaac908-1db3-4b5c-a7ae-c040b9684403")
        let configuration = ChatUIConfiguration(widgetId: "UWiEkKvdAaUJ1xut",
                                                username: "2989c53a-d0c5-4222-af8d-fbf7b0c74ec6",
                                                connection: connection, // null to disable connection api
                                                theme: .light)

        let components = ChatUIComponents(
            onMounted: { context in
                print("onMounted", context)
            },
            onHeaderSelected: { channel in
                print("onHeaderSelected", channel)
            },
            onMenuActionSelected: { action in
                print("onMenuActionSelected", action)
            }
        )

        let view = ChatUIView(configuration: configuration,
                              components: components)
        view.translatesAutoresizingMaskIntoConstraints = false
        self.view.addSubview(view)
        return view
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        NSLayoutConstraint.activate([
            chatUi.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            chatUi.leadingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.leadingAnchor),
            chatUi.trailingAnchor.constraint(equalTo: view.safeAreaLayoutGuide.trailingAnchor),
            chatUi.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
}

