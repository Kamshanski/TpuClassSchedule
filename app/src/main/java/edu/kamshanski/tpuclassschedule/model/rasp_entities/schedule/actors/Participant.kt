package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors

import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.ParticipantType

open class Participant(
    val name: String,
    val link: String) {

    companion object {
        fun getParticipantType(link: String) : ParticipantType? {
            return ParticipantType.values().find { link.contains(it.linkMarker) }
        }
    }

    override fun toString(): String {
        return "$name : $link"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Participant

        if (name != other.name) return false
        if (link != other.link) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + link.hashCode()
        return result
    }


}