package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule

import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.ActivityType
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.utils.nice_classes.requireString

data class Activity(val name: String,
               val fullName: String,
               val type: ActivityType,
               val link: String,
               val contexts: List<Context>
) {
    class Builder(
        var name: String? = null,
        var fullName: String? = null,
        var type: ActivityType? = null,
        var link: String? = null,
        private val contexts: MutableList<Context> = ArrayList(0)
    ) {
        /** @return null if name or fullName is absent */
        fun build() : Activity? {
            if ((name.isNullOrBlank() && fullName.isNullOrBlank()) || type == null)
                return null

            if (name.isNullOrBlank())
                name = fullName
            if (fullName.isNullOrBlank())
                fullName = name

            return Activity(
                name!!,
                fullName!!,
                type!!,
                link ?: "",
                if (contexts.isEmpty()) emptyList() else contexts )
        }

        fun add(context: Context?) {
            if (context != null)
                contexts.add(context)
        }
    }
}
