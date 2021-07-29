package edu.kamshanski.tpuclassschedule.model.preferences.base

import edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences.PreferenceConverter
import edu.kamshanski.tpuclassschedule.utils.nice_classes.gson

class GsonPreferenceConverter<T>(private val clazz: Class<T>) : PreferenceConverter<T, String> {
    override fun format(originalValue: T): String = gson.toJson(originalValue)

    override fun parse(savedValue: String): T = gson.fromJson(savedValue, clazz)
}