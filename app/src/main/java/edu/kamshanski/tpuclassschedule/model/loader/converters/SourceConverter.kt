package edu.kamshanski.tpuclassschedule.model.loader.converters

import com.company.sequential_parser.parse
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo
import edu.kamshanski.tpuclassschedule.utils.string.sequential_parser.SequentialParser
import okhttp3.ResponseBody
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
open class SourceConverter : AbstractSchedulePageConverter<SourceResponse>() {

    override fun convert(value: ResponseBody?): SourceResponse {
        value!!
        val parser = SequentialParser(value.string())

        val lyw = getLinkYearWeek(parser)
        val participantType = Participant.getParticipantType(lyw.link)

        val encryptKey = getEncryptKey(parser)

        val info: ParticipantInfo = getInfo(parser, encryptKey, participantType)

        return SourceResponse(lyw.link, info)
    }


}