package edu.kamshanski.tpuclassschedule.model.loader.converters

import edu.kamshanski.tpuclassschedule.model.loader.responses.WeekScheduleResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.utils.string.sequential_parser.SequentialParser
import kotlinx.datetime.Clock
import okhttp3.ResponseBody
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime
@ExperimentalTime
@ExperimentalContracts
class WeekScheduleConverter(val yearTimeRanges: Set<YearTimeRange>) : AbstractSchedulePageConverter<WeekScheduleResponse>() {
    override fun convert(value: ResponseBody?): WeekScheduleResponse {
        value!!
        val parser = SequentialParser(value.string())

        val lyw = getLinkYearWeek(parser)
        val participantType = Participant.getParticipantType(lyw.link)

        val encryptKey = getEncryptKey(parser)

        val info = getInfo(parser, encryptKey, participantType)

        val weekMetaInfo = getWeekScheduleMetaInfo(parser)

        val yearReference = yearTimeRanges.find { it.year == lyw.year }!!
        val weekSchedule = getWeekSchedule(parser, yearReference, lyw.week, encryptKey)

        val loadingTime = getLoadingTime(parser)
        // Применить timeZone к Clock.System.now()
        return WeekScheduleResponse(loadingTime, Clock.System.now(), info, weekMetaInfo, weekSchedule)
    }

}