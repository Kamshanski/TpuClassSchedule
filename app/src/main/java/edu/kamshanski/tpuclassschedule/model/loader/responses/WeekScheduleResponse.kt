package edu.kamshanski.tpuclassschedule.model.loader.responses

import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.WeekSchedule
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.OtherInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo
import kotlinx.datetime.Instant

data class WeekScheduleResponse(val updateTime: Instant,
                                val loadingTime: Instant,
                                val sourceInfo: ParticipantInfo,
                                val scheduleMeta: List<OtherInfo>,
                                val schedule: WeekSchedule,
)