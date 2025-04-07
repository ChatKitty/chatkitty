import Foundation

final class StompXLogger {
    static func logDebug(_ text: String, file: String = #file, line: Int = #line) {
        print("[STOMP-X][\(file)][\(line)] - \(text)")
    }
    
    static func logError(_ text: String, file: String = #file, line: Int = #line) {
        print("[STOMP-X][ERROR][\(file)][\(line)] - \(text)")
    }
}
