package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule

import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.utils.nice_classes.checkInRange
import edu.kamshanski.tpuclassschedule.utils.nice_classes.checkNonNegative
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant

// TODO: заменить все Instant на LocalDateTime!!!
data class Window(
    val from: DateTimePeriod,
    val till: DateTimePeriod,
    @IntRange(from = 0, to = 6) val ordinal: Int,
    val activities: List<Activity>) : Iterable<Activity> {
    init {
        checkInRange(ordinal, from = 0, to = 6) { "Week ordinal is out of range" }
    }
    override fun iterator(): Iterator<Activity> = activities.iterator()

    class Builder(
        var from: DateTimePeriod? = null,
        var till: DateTimePeriod? = null,
        var ordinal: Int = -1,
        private val activities: MutableList<Activity> = ArrayList(0)
    ) {
        fun build() : Window? {
            if (!isEmpty() && ordinal > -1 && till != null && from != null) {
                return Window(from!!, till!!, ordinal, activities)
            }
            return null
        }

        fun add(activity: Activity?) {
            if (activity != null) {
                activities.add(activity)
            }
        }

        fun isEmpty() = activities.isEmpty()
    }

}
