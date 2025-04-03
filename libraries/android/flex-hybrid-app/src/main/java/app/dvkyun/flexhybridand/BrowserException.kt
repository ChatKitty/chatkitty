package app.dvkyun.flexhybridand

class BrowserException {

    val reason: String?

    constructor(Reason: String) {
        reason = Reason
    }

    constructor() {
        reason = null
    }

}