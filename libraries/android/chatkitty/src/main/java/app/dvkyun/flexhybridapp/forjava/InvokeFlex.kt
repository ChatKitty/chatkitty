package app.dvkyun.flexhybridapp.forjava

import app.dvkyun.flexhybridapp.FlexArguments

interface InvokeFlex<R> {
    fun invoke(arguments: FlexArguments): R
}