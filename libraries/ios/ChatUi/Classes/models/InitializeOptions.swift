struct InitializeOptions: Codable {
    let username: String
    let theme: Theme?
    var environment: String? = nil
    var authentication: Authentication? = nil
    var clientSpecification: ClientSpecification? = nil
}

struct ClientSpecification: Codable {
    let version: String
    let environment: String
    let connection: String
    
    init(connection: String) {
        self.version = ChatKittyUI.version
        #if DEBUG
        self.environment = "development"
        #else
        self.environment = "production"
        #endif
        self.connection = connection
    }
}
