package app.dvkyun.flexhybridand.forjava

import app.dvkyun.flexhybridand.FlexAction
import app.dvkyun.flexhybridand.FlexArguments

interface InvokeAction {
    fun invoke(action: FlexAction, arguments: FlexArguments)
}