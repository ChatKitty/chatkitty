package app.dvkyun.flexhybridapp.forjava

import app.dvkyun.flexhybridapp.FlexAction
import app.dvkyun.flexhybridapp.FlexArguments

interface InvokeAction {
    fun invoke(action: FlexAction, arguments: FlexArguments)
}