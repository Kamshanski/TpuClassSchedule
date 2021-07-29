package edu.kamshanski.tpuclassschedule.model.preferences

import android.content.Context
import edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences.AbstractPreferences
import edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences.boolPref
import edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences.instantPref
import edu.kamshanski.tomskpolytechnicuniversityclassschedule.model.preferences.setPref
import edu.kamshanski.tpuclassschedule.model.preferences.base.GsonPreferenceConverter
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.model.time.RaspClock
//todo:  для activity с link (т.е. event'ов) использовать полное название (либо сделать отдельную настройку)
class CommonPreferences(context: Context) : AbstractPreferences(context) {
    override val preferencesName: String
        get() = "COMMON_PREFERENCES"

    var firstLaunch by boolPref("firstLaunch", true)

    var lastUpdateTime by instantPref("lastUpdateTime", RaspClock.NULL_TIME)

    var yearsTimeRanges by setPref<YearTimeRange>(
        "yearsTimeRanges",
        HashSet(0),
        GsonPreferenceConverter(YearTimeRange::class.java))

    var selectedSources by setPref<ShortSource>(
        "selectedSources",
        HashSet(0),
        GsonPreferenceConverter(ShortSource::class.java)
    )
}