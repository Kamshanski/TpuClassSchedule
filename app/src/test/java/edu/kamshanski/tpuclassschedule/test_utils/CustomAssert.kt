package edu.kamshanski.tpuclassschedule.test_utils

import org.junit.jupiter.api.fail
import org.junit.jupiter.api.Assertions.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract


fun <T> assertEqualContent(o1: List<T>, o2: List<T>, matcher: (T, T) -> Boolean) {
    if (!sameContent(o1, o2, matcher))
        assertEquals(o1, o2) {
            "\nExpected: $o1" +
            "\nActual:   $o2"
        }
}


public fun <A, B> assertBothEqual(value: Pair<A?, B?>) : Pair<A?, B?> {
    if ((value.first == null) xor (value.second == null)) {
        fail("One of pair values is null, while another one is not: ${value.first}; ${value.second}")
    } else {
        return value
    }
}

@ExperimentalContracts
inline fun <T> assertIfNotNull(value: T?, block: (T) -> Unit) : T {
    if (value != null) {
        block(value)
        return value
    }
    assertNotNull(value)
    // Unreachable code
    return Any() as T
}
@ExperimentalContracts
inline fun <T> assertEqualsIfNotNull(v1: T?, v2: T?, msg: String? = null, block: (T, T) -> Unit) : Boolean {
    if (v1 != null && v2 != null) {
        block(v1, v2)
        return true
    } else {
        fail(msg ?: "${if (v1 == null) "first" else "second"} " +
        "value is null while the other one is: ${v1 ?: v2}")
    }
}

inline fun <T> sameContent(f: List<T>, s: List<T>, matcher: (T, T) -> Boolean) : Boolean {
    if (f.size != s.size)
        return false

    if (f.isEmpty())
        return true

    val nonNullF = f.filterNotNull()
    val nonNullS = s.filterNotNull()

    if (nonNullF.size != nonNullS.size)
        return false

    if (nonNullF.isEmpty())
        return true

    outer@ for (ef in nonNullF) {
        for (es in nonNullS) {
            if (matcher(ef, es)) {
                continue@outer
            }
        }
        return false
    }
    return true
}