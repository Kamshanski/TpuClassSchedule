package edu.kamshanski.tpuclassschedule.utils.primitives

fun Int.toFormatString(leadingZeros: Int) : String {
    return String.format("%0" + leadingZeros + "d", this)
}

inline fun Int.isEven() : Boolean = this and 1 == 0
inline fun Int.isOdd() : Boolean = this and 1 == 1
inline fun Long.isEven() : Boolean = this and 1L == 0L
inline fun Long.isOdd() : Boolean = this and 1L == 1L

inline infix fun Char.xor(other: Char): Char {
    return (this.toInt() xor other.toInt()).toChar()
}
inline infix fun Byte.xor(other: Byte): Byte {
    return (this.toInt() xor other.toInt()).toByte()
}

/** Does nothing. Use as a placeholder.
 * For example. If there exist some branch in when() that is better to be considered
 * affects nothing actually
 */
inline fun pass() {}

inline fun Boolean.toInt() : Int = if (this) 1 else 0