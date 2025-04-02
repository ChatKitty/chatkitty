package app.dvkyun.flexhybridand

class FlexArguments internal constructor(private val array: Array<FlexData>) : FlexType {

    operator fun get(index: Int): FlexData? {
        return try {
            array[index]
        } catch (e: ArrayIndexOutOfBoundsException) {
            null
        }
    }

    internal operator fun set(index: Int, value: FlexData) {
        array[index] = value
    }

    val size: Int get() = array.size

    fun forEach(action: (FlexData) -> Unit) {
        for (element in array) action(element)
    }


    fun toArray(): Array<FlexData> = array

    operator fun iterator(): Iterator<FlexData> = array.iterator()

    fun asIterable(): Iterable<FlexData> {
        if (array.isEmpty()) return emptyList()
        return Iterable { this.iterator() }
    }

    fun toList(): List<FlexData> {
        return when (size) {
            0 -> emptyList()
            1 -> listOf(array[0])
            else -> toMutableList()
        }
    }

    fun toMutableList(): MutableList<FlexData> = toArrayList()

    fun toArrayList(): ArrayList<FlexData> = ArrayList(asCollection())

    private fun asCollection(): Collection<FlexData> = ArrayAsCollection()

    private inner class ArrayAsCollection : Collection<FlexData> {
        override val size: Int get() = array.size
        override fun isEmpty(): Boolean = array.isEmpty()
        override fun contains(element: FlexData): Boolean = array.contains(element)
        override fun containsAll(elements: Collection<FlexData>): Boolean =
            elements.all { contains(it) }

        override fun iterator(): Iterator<FlexData> = array.iterator()
        fun toArray(): Array<FlexData> =
            java.util.Arrays.copyOf(array, this.size, Array<FlexData>::class.java)
    }

}