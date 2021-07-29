package edu.kamshanski.tpuclassschedule.model.loader.responses

import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo

class SourceResponse(var link: String,
                     var info: ParticipantInfo)