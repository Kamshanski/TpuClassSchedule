package edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences

import android.annotation.SuppressLint
import android.content.SharedPreferences
import edu.kamshanski.tpuclassschedule.model.time.RaspClock
import kotlinx.datetime.Instant
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
// https://habr.com/ru/post/461161/#comment_20443827

abstract class PreferencesDelegate<T>(val key: String) : ReadWriteProperty<SharedPreferencesHolder, T>

abstract class AbstractPreferencesDelegate<T>(key: String,
                                              val defaultValue: T,
                                              val getter: SharedPreferences.(key: String, defVal: T) -> T,
                                              val setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor,
) : PreferencesDelegate<T>(key) {
    @SuppressLint("CommitPrefEdits")
    final override fun setValue(thisRef: SharedPreferencesHolder, property: KProperty<*>, value: T) {
        setter(thisRef.prefs.edit(), key, value).apply()
    }

    final override fun getValue(thisRef: SharedPreferencesHolder, property: KProperty<*>): T {
        return getter(thisRef.prefs, key, defaultValue)
    }
}

abstract class AbstractPreferencesDelegateWithLazyDefault<T>(
    key: String,
    val defaultValue: () -> T,
    val getter: SharedPreferences.(key: String, defVal: () -> T) -> T,
    val setter: SharedPreferences.Editor.(key: String, value: T) -> SharedPreferences.Editor,
) : PreferencesDelegate<T>(key) {
    final override fun setValue(thisRef: SharedPreferencesHolder, property: KProperty<*>, value: T) {
        setter(thisRef.prefs.edit(), key, value).apply()
    }

    final override fun getValue(thisRef: SharedPreferencesHolder, property: KProperty<*>): T {
        return getter(thisRef.prefs, key, defaultValue)
    }
}

class BooleanPreferencesDelegate(key: String,
                                 defaultValue: Boolean
) : AbstractPreferencesDelegate<Boolean>(key,
                                         defaultValue,
                                         SharedPreferences::getBoolean,
                                         SharedPreferences.Editor::putBoolean)


class StringPreferencesDelegate(key: String,
                                defaultValue: String
) : AbstractPreferencesDelegate<String>(
    key, defaultValue,
    { k, defVal -> getString(k, defVal) ?: defVal },
    SharedPreferences.Editor::putString
)

class IntPreferencesDelegate(key: String,
                             defaultValue: Int
) : AbstractPreferencesDelegate<Int>(
    key, defaultValue,
    SharedPreferences::getInt,
    SharedPreferences.Editor::putInt
)

class LongPreferencesDelegate(key: String,
                              defaultValue: Long
) : AbstractPreferencesDelegate<Long>(
    key, defaultValue,
    SharedPreferences::getLong,
    SharedPreferences.Editor::putLong
)

class StringSetPreferencesDelegate(key: String,
                                   defaultValue: () -> Set<String>
) : AbstractPreferencesDelegateWithLazyDefault<Set<String>>(
    key, defaultValue,
    { k, _ -> getStringSet(k, null) ?: defaultValue() },
    SharedPreferences.Editor::putStringSet
)

class FloatPreferencesDelegate(key: String,
                               defaultValue: Float
) : AbstractPreferencesDelegate<Float>(
    key, defaultValue,
    SharedPreferences::getFloat,
    SharedPreferences.Editor::putFloat
)

// custom preferences type

class InstantPreferencesDelegate(key: String,
                               defaultValue: Instant
) : AbstractPreferencesDelegate<Instant>(
    key, defaultValue,
    { k, d ->
        val formatted = getString(k, null)
        if (formatted == null) d else Instant.parse(formatted)
    },
    { k, value -> putString(k, value.toString()) }
)

class SetPreferencesDelegate<T> (key: String,
                                 defaultValue: MutableSet<T>,
                                 val converter: PreferenceConverter<T, String>
) : AbstractPreferencesDelegate<MutableSet<T>>(
    key, defaultValue,
    { k, d ->
        val result = getStringSet(k, emptySet())?.let {
            val set = HashSet<T>(it.size)
            for (encodedValue in it) {
                val decodedValue = converter.parse(encodedValue) ?: return@let null
                set.add(decodedValue)
            }
            set
        }
        result ?: d
    },
    { k, value ->
        val stringSet = HashSet<String>(value.size)
        for (originalValue in value) {
            stringSet.add(converter.format(originalValue))
        }
        putStringSet(k, stringSet)
    }
)


fun SharedPreferencesHolder.boolPref(key: String, defValue: Boolean = false) = BooleanPreferencesDelegate(key, defValue)
fun SharedPreferencesHolder.intPref(key: String, defValue: Int = 0) = IntPreferencesDelegate(key, defValue)
fun SharedPreferencesHolder.longPref(key: String, defValue: Long = 0L) = LongPreferencesDelegate(key, defValue)
fun SharedPreferencesHolder.floatPref(key: String, defValue: Float = 0f) = FloatPreferencesDelegate(key, defValue)
fun SharedPreferencesHolder.stringPref(key: String, defValue: String = "") = StringPreferencesDelegate(key, defValue)
fun SharedPreferencesHolder.stringSetPref(key: String, defValue: () -> Set<String> = { HashSet(1) }) = StringSetPreferencesDelegate(key, defValue)
// custom preferences type
fun SharedPreferencesHolder.instantPref(key: String, defValue: Instant = RaspClock.NULL_TIME) = InstantPreferencesDelegate(key, defValue)
fun <T> SharedPreferencesHolder.setPref(key: String, defValue: MutableSet<T>, converter: PreferenceConverter<T, String>) = SetPreferencesDelegate(key, defValue, converter)

interface PreferenceConverter<T, V> {
    fun format(originalValue: T) : V
    fun parse(savedValue: V) : T?
}