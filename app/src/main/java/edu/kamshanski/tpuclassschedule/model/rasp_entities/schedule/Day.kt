package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule

import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.DayRoutine
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant

data class Day(
        val date: Instant,
        val dayOfWeek: DayOfWeek,
        val dayRoutine: DayRoutine,
        val windows: List<Window>
) : Iterable<Window> {
    override fun iterator(): Iterator<Window> = windows.iterator()

    class DayBuilder(
        var date: Instant? = null,
        var dayOfWeek: DayOfWeek? = null,
        var dayRoutine: DayRoutine = DayRoutine.FREE,
        private val windows: MutableList<Window> = ArrayList(0)
    ) {
        fun build() : Day {
            val freeDayHasWindows =
                (DayRoutine.FREE == dayRoutine || DayRoutine.HOLIDAY == dayRoutine) && !isEmpty()
            check (!freeDayHasWindows) { "$dayRoutine can't have any activity. Now it's ${windows.size}: $windows" }
            return Day(date!!, dayOfWeek!!, dayRoutine, windows)
        }

        fun add(window: Window?) {
            if (window != null) {
                windows.add(window)
            }
        }

        fun isEmpty(): Boolean = windows.isEmpty()
    }

    companion object {
        fun getSunday(date: Instant) : Day {
            return Day(date, DayOfWeek.SUNDAY, DayRoutine.FREE, emptyList())
        }
    }
}