package edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta

import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo

class Source(val hash: String,
             name: String,
             link: String,
             val info: ParticipantInfo
) : Participant(name, link)