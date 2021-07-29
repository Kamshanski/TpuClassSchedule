package edu.kamshanski.tpuclassschedule.utils.collections

inline fun <I, reified O> Array<I>.map(transform: (int: Int, value: I) -> O) : Array<O> {
    return Array(this.size) { i -> transform(i, this[i]) }
}

inline fun <T> MutableSet<T>.insert(value: T) : MutableSet<T> {
    add(value)
    return this
}

fun <T> arrayListOf(size: Int, block: (Int) -> T) : ArrayList<T> {
    return ArrayList<T>(size).apply {
        for (i in 0 until size) {
            add(block(i))
        }
    }
}