package app.dvkyun.flexhybridand.forjava

import app.dvkyun.flexhybridand.FlexArguments

interface InvokeFlex<R> {
    fun invoke(arguments: FlexArguments): R
}