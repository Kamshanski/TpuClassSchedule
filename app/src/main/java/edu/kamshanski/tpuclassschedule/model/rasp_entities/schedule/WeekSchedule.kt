package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule

import edu.kamshanski.tpuclassschedule.model.time.RaspClock.checkWeekNumber
import edu.kamshanski.tpuclassschedule.utils.primitives.isOdd
import kotlinx.datetime.Instant

data class WeekSchedule(
        val week: Int,
        val startDate: Instant,
        val days: List<Day>,
) : Iterable<Day> {
    init { checkWeekNumber(week) }

    val isOdd : Boolean
        get() = week.isOdd()

    override fun iterator() : Iterator<Day> = days.iterator()

}