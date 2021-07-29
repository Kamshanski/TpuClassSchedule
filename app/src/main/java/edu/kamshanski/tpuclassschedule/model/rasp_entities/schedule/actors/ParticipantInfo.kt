package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors

data class ParticipantInfo(val fullName: String,
                           val nameAppendix: String = "",
                           val otherInfo: List<OtherInfo> = emptyList())

data class OtherInfo(val info: String,
                     val link: String = "")