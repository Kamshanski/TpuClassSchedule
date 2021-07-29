package edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import edu.kamshanski.tpuclassschedule.utils.nice_classes.gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


interface SharedPreferencesHolder {
    val prefs: SharedPreferences
}

abstract class AbstractPreferences(context: Context) : SharedPreferencesHolder{
    abstract val preferencesName: String
    private val _prefs = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)

    override val prefs: SharedPreferences = _prefs

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun delete(application: Application) {
            application.deleteSharedPreferences(preferencesName)
    }
}

