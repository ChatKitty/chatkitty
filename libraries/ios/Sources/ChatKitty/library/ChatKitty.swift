import Foundation

public final class ChatKitty {
    public static var version: String {
        let semVersion = Bundle(for: ChatKitty.self).infoDictionary?["CFBundleShortVersionString"] as? String ?? "0.0.0"
        return "chatkitty-swift/\(semVersion)"
    }
}

