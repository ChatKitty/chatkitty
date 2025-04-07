import Foundation

public final class ChatKittyUI {
    public static var version: String {
        let semVersion = Bundle(for: ChatKittyUI.self).infoDictionary?["CFBundleShortVersionString"] as? String ?? "0.0.0"
        return "chat-ui-swift/\(semVersion)"
    }
}

