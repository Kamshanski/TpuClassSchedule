package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta

import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.WeekSchedule
import edu.kamshanski.tpuclassschedule.model.time.RaspClock
import kotlinx.datetime.Instant

/**
 * Year time range
 *
 * @property year - full year number, e.g. 2021
 * @property weeks - Number of weeks in the year
 * @property firstDate
 * @property lastDate
 * @constructor Create empty Year time range
 */
data class YearTimeRange(val year: Int, val weeks: Int, val firstDate: Instant, val lastDate: Instant) {
    init {
        RaspClock.checkWeekNumber(weeks)
        RaspClock.checkYearNumber(year)
    }
}