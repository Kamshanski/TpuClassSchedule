package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule

import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant

data class Context(
    val groups: List<Participant>,
    val teachers: List<Participant>,
    val building: Participant?,
    val room: Participant?
) {
    class Builder(val groups: MutableList<Participant> = ArrayList(0),
                  val teachers: MutableList<Participant> = ArrayList(0),
                  var building: Participant? = null,
                  var room: Participant? = null) {

        fun isEmpty() = groups.isEmpty() && teachers.isEmpty() && building == null && room == null

        fun build() : Context? {
            if (isEmpty())
                return null

            return Context(groups, teachers, building, room)
        }
    }



}