package app.dvkyun.flexhybridand

@Suppress("UNCHECKED_CAST")
class FlexData {
    private val data: Any
    var type = Type.NULL
        private set

    enum class Type {
        STRING,
        INT,
        LONG,
        DOUBLE,
        BOOLEAN,
        ARRAY,
        MAP,
        NULL,
        ERR
    }

    internal constructor() {
        data = Any()
    }

    internal constructor(data: String) {
        this.data = data
        type = Type.STRING
    }

    internal constructor(data: Int) {
        this.data = data
        type = Type.INT
    }

    internal constructor(data: Long) {
        this.data = data
        type = Type.LONG
    }

    internal constructor(data: Double) {
        this.data = data
        type = Type.DOUBLE
    }

    internal constructor(data: Boolean) {
        this.data = data
        type = Type.BOOLEAN
    }

    internal constructor(data: Array<FlexData>) {
        this.data = data
        type = Type.ARRAY
    }

    internal constructor(data: Map<String, FlexData>) {
        this.data = data
        type = Type.MAP
    }

    internal constructor(data: BrowserException) {
        this.data = data
        type = Type.ERR
    }

    fun isNull(): Boolean {
        return type == Type.NULL
    }

    fun asString(): String? {
        if (isNull()) return null
        if (type != Type.STRING) throw FlexException(FlexException.ERROR15)
        return data.toString()
    }

    fun asInt(): Int? {
        if (isNull()) return null
        return when (type) {
            Type.INT -> data as Int
            Type.LONG -> (data as Long).toInt()
            Type.DOUBLE -> (data as Double).toInt()
            else -> throw FlexException(FlexException.ERROR15)
        }
    }

    fun asLong(): Long? {
        if (isNull()) return null
        return when (type) {
            Type.INT -> (data as Int).toLong()
            Type.LONG -> data as Long
            Type.DOUBLE -> (data as Double).toLong()
            else -> throw FlexException(FlexException.ERROR15)
        }
    }

    fun asDouble(): Double? {
        if (isNull()) return null
        return when (type) {
            Type.INT -> (data as Int).toDouble()
            Type.LONG -> (data as Long).toDouble()
            Type.DOUBLE -> data as Double
            else -> throw FlexException(FlexException.ERROR15)
        }
    }

    fun asFloat(): Float? {
        if (isNull()) return null
        return when (type) {
            Type.INT -> (data as Int).toFloat()
            Type.LONG -> (data as Long).toFloat()
            Type.DOUBLE -> (data as Double).toFloat()
            else -> throw FlexException(FlexException.ERROR15)
        }
    }

    fun asBoolean(): Boolean? {
        if (isNull()) return null
        if (type != Type.BOOLEAN) throw FlexException(FlexException.ERROR15)
        return data as Boolean
    }

    fun asArray(): Array<FlexData>? {
        if (isNull()) return null
        if (type != Type.ARRAY) throw FlexException(FlexException.ERROR15)
        return data as Array<FlexData>
    }

    fun asMap(): Map<String, FlexData>? {
        if (isNull()) return null
        if (type != Type.MAP) throw FlexException(FlexException.ERROR15)
        return data as Map<String, FlexData>
    }

    fun asErr(): BrowserException? {
        if (isNull()) return null
        if (type != Type.ERR) throw FlexException(FlexException.ERROR15)
        return data as BrowserException
    }

    override fun toString(): String {
        return data.toString()
    }

    inline fun <reified T> reified(): T? {
        if (isNull()) return null
        if (T::class == String::class) {
            return asString() as T
        } else if (T::class == Int::class) {
            return asInt() as T
        } else if (T::class == Long::class) {
            return asLong() as T
        } else if (T::class == Double::class) {
            return asDouble() as T
        } else if (T::class == Float::class) {
            return asFloat() as T
        } else if (T::class == Boolean::class) {
            return asBoolean() as T
        } else if (T::class == Array::class) {
            return asArray() as T
        } else if (T::class == Map::class) {
            return asMap() as T
        } else if (T::class == BrowserException::class && type == Type.ERR) {
            return asErr() as T
        } else {
            throw FlexException(FlexException.ERROR15)
        }
    }

}


