package edu.kamshanski.tpuclassschedule.model.loader.converters

import androidx.annotation.IntRange
import com.company.sequential_parser.SearchToken
import com.company.sequential_parser.parse
import edu.kamshanski.tpuclassschedule.model.loader.Decryptor
import edu.kamshanski.tpuclassschedule.model.loader.Decryptor.raspDecode
import edu.kamshanski.tpuclassschedule.model.loader.LinkYearWeek
import edu.kamshanski.tpuclassschedule.model.loader.isRaspSourceUrl
import edu.kamshanski.tpuclassschedule.model.loader.parseRaspUrl
import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.ActivityType
import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.DayRoutine
import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.ParticipantType
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.*
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.OtherInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.model.time.RaspClock
import edu.kamshanski.tpuclassschedule.model.time.RaspClock.raspUpdateToInstant
import edu.kamshanski.tpuclassschedule.utils.nice_classes.requireString
import edu.kamshanski.tpuclassschedule.utils.string.clearWhitespaces
import edu.kamshanski.tpuclassschedule.utils.string.sequential_parser.SequentialParser
import edu.kamshanski.tpuclassschedule.utils.collections.arrayListOf
import edu.kamshanski.tpuclassschedule.utils.lg
import edu.kamshanski.tpuclassschedule.utils.nice_classes.tryToGet
import edu.kamshanski.tpuclassschedule.utils.string.contains
import edu.kamshanski.tpuclassschedule.utils.string.isNotNullOrBlank
import edu.kamshanski.tpuclassschedule.utils.string.removePostfix
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import okhttp3.ResponseBody
import retrofit2.Converter
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
abstract class AbstractSchedulePageConverter<R> : Converter<ResponseBody, R> {
    // https://examples.javacodegeeks.com/core-java/util/regex/greedy-and-non-greedy-reg-ex-matching/
    val dotMatchedAll = setOf(RegexOption.DOT_MATCHES_ALL)
    val tagRegex = Regex("<.*?>", dotMatchedAll)
    val tagAndBracketsRegex = Regex("<.*?>|\\(.*?\\)", dotMatchedAll)
    val teacherPositionRegex = Regex("<p .*>\\s*([А-Яа-я\\s\\(\\)\\:\\,\\.]*)\\s*<\\/p") // DON'T APPLY dotMatchesAll
    val linkRegex = Regex("https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*")
    val participantRegex = Regex("<a class[\\w\\?\\/\\\\\\=\\\"\\s\\&]*href=\\\"([\\w\\/\\\\\\=\\.]*)[\\w\\?\\/\\\\\\=\\.\\&\\s]*\\\">([А-Яа-я0-9\\s\\.\\/\\-]*)</a>")

    val linkMarkers = arrayOf(
        "og:url\" content=\"https://rasp.tpu.ru/" to "\">",
        "twitter:url\" content=\"https://rasp.tpu.ru/" to "\">"
    )
    val encryptKeyMarkers = "encrypt\" content=\"" to "\">"
    val TIME_COLUMN = 0

    fun checkCorrectResponse(src: String) : Boolean {
        if (src.length < 600) {
            val errorMessages = arrayOf(
                "Указана неправильная неделя сводного линейного графика",
            )
            for (msg in errorMessages) {
                if (src.contains(msg)) {
                    TODO()
                }
            }
            TODO()
        }
        return true
    }

    fun getLinkYearWeek(parser: SequentialParser) : LinkYearWeek {
        var _lyw: LinkYearWeek? = null

        // поиск link
        for (markers in linkMarkers) {
            if (_lyw == null) {
                parser.search(markers, times = SearchToken.ANY, limit = 1) { url, _ ->
                    if (url.isNotBlank() && isRaspSourceUrl(url)) {
                        _lyw = parseRaspUrl(url)
                    }
                }
            }
            else break
        }
        val lyw = checkNotNull(_lyw) { "Link Year and Week cannot be null" }
        requireString(lyw.link.removePostfix("?", isPostfixBeginning = true))
        return lyw
    }

    fun getEncryptKey(parser: SequentialParser) : String {
        var encryptKey = parser.search(encryptKeyMarkers) { it, _ ->
            check(it.isNotBlank()) { "Encrypt key cannot be blank!" }
        }
        return requireString(encryptKey) { "Encrypt key cannot be blank!" }
    }

    fun getInfo(parser: SequentialParser, encryptKey: String, type: ParticipantType?) : ParticipantInfo {
        var fullName = ""
        var addName = ""
        val otherInfo = ArrayList<OtherInfo>(3)
        parser.searchOneOrNull("panel panel-flat", "md-3") { src, _ ->
            val infoParser = SequentialParser(src)

            val zip = nameInfo(infoParser, encryptKey)
            fullName = zip.first
            addName = zip.second

            when(type) {
                ParticipantType.GROUP,
                ParticipantType.ROOM,
                ParticipantType.BUILDING -> groupOrPlaceOtherInfo(infoParser, otherInfo)
                ParticipantType.TEACHER -> teacherOtherInfo(infoParser, otherInfo)
                else -> lg("Unpredicted behaviour! Participant is not null!")
            }
        }
        return ParticipantInfo(fullName, addName, otherInfo)
    }

    fun nameInfo(parser: SequentialParser, encryptKey: String) : Pair<String, String> {
        val block = parser.search("<h5 class=\"panel-title\">", "</h5>")!!

        val encryptPattern = Regex("data-encrypt=\\\"(.*)\\\"")
        var fullName = encryptPattern
            .find(block)
            ?.destructured
            ?.component1()
            ?.removeSurrounding("(", ")")
        if (fullName != null) {
            fullName = Decryptor.decode(fullName, encryptKey)
        } else {
            fullName = block.replace(tagAndBracketsRegex, "").clearWhitespaces()
        }
        val additionalInfo = Regex("(\\(.*\\))", dotMatchedAll)
            .find(block)
            ?.destructured
            ?.component1()
            ?.replace(tagRegex, " ")
            ?.removeSurrounding("(", ")")
            ?.clearWhitespaces() ?: ""

        return fullName to additionalInfo
    }

    fun groupOrPlaceOtherInfo(parser: SequentialParser, otherInfoList: ArrayList<OtherInfo>) {
        val block = parser.searchOneOrNull("\"media-left\"","</ul>")
        block?.parse()?.search("<li>", "</li>", times = SearchToken.ANY) { it, _ ->
            val link = linkRegex
                .find(it)
                ?.destructured?.match?.groupValues?.get(0)
                ?: ""
            val info = it.replace(tagRegex, "").clearWhitespaces()
            if (info.isNotBlank()) {
                otherInfoList.add(OtherInfo(info, link))
            }
        }
    }

    fun teacherOtherInfo(parser: SequentialParser, otherInfoList: ArrayList<OtherInfo>)  {
        parser.moveTo("col-md-5")

        parser.searchOneOrNull("\"media-left\">", "<div")?.let {
            val matches = teacherPositionRegex.findAll(it)
            for (match in matches) {
                val position = match.destructured.component1().clearWhitespaces()
                if (position.isNotBlank()) {
                    otherInfoList.add(OtherInfo(position))
                }
            }
        }

        val contactsSrc = parser.searchOneOrNull("media-annotation", "class=\"col")
        contactsSrc?.parse()?.also { contactsParser ->
            contactsParser.search("href=\"tel:", "\">", times = SearchToken.ANY) { phone, _ ->
                if (phone.isNotNullOrBlank()) {
                    otherInfoList.add(OtherInfo(phone))
                }
            }

            val email = contactsParser.searchOneOrNull("href=\"mailto:", "\">")
            if (email.isNotNullOrBlank()) {
                otherInfoList.add(OtherInfo(email))
            }
        }

    }

    fun getWeekScheduleMetaInfo(parser: SequentialParser) : List<OtherInfo> {
        val infoList = ArrayList<OtherInfo>(1)
        parser.search("col-md-4", "previous") { ulTag, _ ->
            ulTag.parse().search("<li>", "</li>", times = SearchToken.ANY, limit = SearchToken.UNLIMITED) { it, _ ->
                val info = it.replace(tagRegex, "").trim()
                if (info.isNotBlank()) {
                    infoList.add(OtherInfo(info))
                }
            }
        }
        return infoList
    }

    fun getWeekSchedule(parser: SequentialParser, yearReference: YearTimeRange, week: Int, encryptKey: String) : WeekSchedule {
        // 0 col - time, 1 col - monday, ..., 6 col - saturday
        // 0 row - 8.30-10.05, ..., 6 row - 20:20-21:55
        val considerableColumns = arrayListOf(7) { it } // no need in time column // TODO: consider time as it's needed in window
        val dayBuilders = Array(7) { i ->
            val dayOfWeek = RaspClock.dayOfWeek(i)
            val date = RaspClock.getDateOf(yearReference, week, dayOfWeek)
            Day.DayBuilder(date = date, dayOfWeek = dayOfWeek)
        }

        parser.search("<tr ", "</tr>", times = 7) { rowString, rowTime ->
            val activityOrdinal = rowTime // номер пары ( от 0 до 6)
            var activityStart = DateTimePeriod()
            var activityEnd = DateTimePeriod()
            rowString.parse()
                .search("<td ", "</td>", times = SearchToken.ANY, limit = 7)
                cell@ { cell, colTime ->
                    val firstChar = cell[0]
                    if (considerableColumns.contains(colTime)) {
                        if (colTime == TIME_COLUMN) {
                            if (firstChar == 't') {                         // d title=
                                cell.parse().search("title=\"", "\"")?.let {
                                    activityStart = tryToGet(DateTimePeriod())  {
                                        DateTimePeriod(
                                            hours = it.substring(0, 2).toInt(),
                                            minutes = it.substring(3, 5).toInt())
                                    }
                                    activityEnd = tryToGet(DateTimePeriod())  {
                                        DateTimePeriod(
                                            hours = it.substring(8, 10).toInt(),
                                            minutes = it.substring(11, 13).toInt())
                                    }
                                }
                            }
                            else  lg("unexpected behaviour in getWeekSchedule(parser, $yearReference, $week, $encryptKey)!") // TODO: construct adequate log
                        }
                        else if (hasActivityContent(cell)) {
                            val dayNum = colTime - 1
                            val dayBuilder = dayBuilders[dayNum]
                            when (firstChar) {
                                'c' -> {  // d class=
                                    fillWorking(
                                        cell, dayBuilder, activityStart,
                                        activityEnd, activityOrdinal, encryptKey
                                    )
                                    val isFree = dayBuilder.isEmpty()
                                    dayBuilder.dayRoutine = if (isFree) DayRoutine.FREE else DayRoutine.WORKING
                                }
                                'r' -> {  // d rowspan=
                                    considerableColumns.remove(colTime)
                                    dayBuilder.dayRoutine = DayRoutine.HOLIDAY
                                }
                                else -> lg("unexpected behaviour in getWeekSchedule(parser, $yearReference, $week, $encryptKey)!") // TODO: construct adequate log
                            }
                        }
                    }
                }
        }
        val days = dayBuilders.map { day ->
            if (day.dayRoutine != DayRoutine.HOLIDAY) {
                day.dayRoutine =
                    if (day.dayOfWeek == DayOfWeek.SUNDAY) {
                         DayRoutine.HOLIDAY
                    } else if (day.isEmpty()) {
                        DayRoutine.FREE
                    } else {
                        DayRoutine.WORKING
                    }
            }
            day.build()
        }
        val schedule = WeekSchedule(week, RaspClock.getDateOf(yearReference, week, DayOfWeek.MONDAY), days)
        return schedule
    }

    fun hasActivityContent(cell: String) : Boolean {
        // Examples of empty cells:
        //    <td class="cell  hidden-xs"></td>
        //    <td class="cell "></td>
        // rowString parser in [getWeekSchedule] ends on "</td>". If next "<" after "<t"
        // is the last char, then the cell has no activity content
        val indexOfTagCloseDiamondBracket = cell.indexOf('>')
        return indexOfTagCloseDiamondBracket != cell.lastIndex
    }

    fun fillWorking(src: String,
                    dayBuilder: Day.DayBuilder,
                    activityStart: DateTimePeriod,
                    activityEnd: DateTimePeriod,
                    @IntRange(from = 0, to = 6) classNumber: Int,
                    encryptKey: String) {
        RaspClock.checkClassNumber(classNumber)
        val windowBuilder = Window.Builder(from = activityStart, till = activityEnd, ordinal = classNumber)
        var activityBuilder = Activity.Builder()

        val parts = src.split("<hr")
        for (part in parts) {
            val context = Context.Builder()

            part.parse().search("<div>", "</div>", times = SearchToken.ANY)
            { piece, times ->
                if (times == 0) {   // activityType всегда в только начальном блоке <div>
                    val activityType = findActivityType(piece)
                    if (activityType != null) {
                        // first emtpty builder will be skipped as activityBuilder.build() with null
                        // activityType gives null that is skipped by windowBuilder.add()
                        windowBuilder.add(activityBuilder.build())
                        activityBuilder = Activity.Builder(type = activityType)
                    }
                }
                if (times == 0 || times == 1) {
                    var foundNames: Pair<String?, String?>? = null
                    if (activityBuilder.link.isNullOrBlank()) {
                        activityBuilder.link = findEventLink(piece)
                    }
                    if (activityBuilder.name.isNullOrBlank()) {
                        foundNames = findEventName(piece, encryptKey)
                        activityBuilder.name = foundNames.first
                        activityBuilder.fullName = foundNames.second
                    }
                    if (foundNames?.first != null) {
                        // if found name, then new activity was found. no reason to search for
                        // activity context
                        return@search
                    }
                }

                val participantMatches = participantRegex.findAll(piece)
                for (pm in participantMatches) {
                    val link = pm.destructured.component1().trim()
                    val name = pm.destructured.component2().trim()
                    val participant = Participant(name, link)

                    val contentType = Participant.getParticipantType(link)
                    when(contentType) {
                        ParticipantType.GROUP -> context.groups.add(participant)
                        ParticipantType.TEACHER -> context.teachers.add(participant)
                        ParticipantType.BUILDING -> context.building = participant
                        ParticipantType.ROOM -> context.room = participant
                        else -> lg("Unpredicted behaviour! Participant type is unknown: " +
                                pm.destructured.component2()
                        )
                    }
                }
            }
            activityBuilder.add(context.build())
        }
        windowBuilder.add(activityBuilder.build())
        dayBuilder.add(windowBuilder.build())
    }

    private fun findActivityType(piece: String) : ActivityType? {
        val parser = piece.parse()
        parser.moveTo("<b title=\"")
        val found = parser.searchOneOrNull("\">", "</b>")

        if (found.isNullOrBlank()) {
            return null
        }
        return ActivityType.getByAcronym(found)
    }

    private fun findEventLink(piece: String) : String? {
        val link = piece.parse().searchOneOrNull("href=\"/event", "\">")
        if (link != null) {
            return "/event" + link
        } else {
            return null
        }
    }

    private fun findEventName(piece: String, encryptKey: String ) : Pair<String?, String?> {
        val onPosition = piece.indexOfFirst { !it.isWhitespace() }
        if (piece.contains("<span", onPosition)) {   // contains encoded data
            val parser = piece.parse()
            val name = parser.search("data-encrypt=\"", "\"")
                ?.raspDecode(encryptKey)
            val fullName = parser.search("data-title=\"", "\"")
                ?.raspDecode(encryptKey)
            return name to fullName

        } else {    // simple data
            val fullName = piece.replace(tagAndBracketsRegex, "").trim()
            return null to fullName
        }
    }

    private fun fillGroupsOrTeachers(piece: String, contextBuilder: Context.Builder) {
        var participantList: MutableList<Participant>? = null
        val contextSources = piece.split(',')
        for (ctxSrc in contextSources) {
            val link = piece.parse().search("href=\"", "\"")!!
                .removePostfix("?", isPostfixBeginning = true)
            val name = piece.parse().search(">", "<")!!
            if (participantList == null) {
                val contentType = Participant.getParticipantType(link)
                participantList = when(contentType) {
                    ParticipantType.GROUP -> contextBuilder.groups
                    ParticipantType.TEACHER -> contextBuilder.teachers
                    else -> {
                        lg("Unpredicted link! link: $link in piece: $piece")
                        null
                    }
                }
            }
            participantList?.add(Participant(name, link))
        }
    }

    private fun fillBuildingAndRoom(piece: String, contextBuilder: Context.Builder) {
        val parser = piece.parse()
        val linkBuilding = parser.searchOneOrNull("href=\"/sooruzhenie", "\"")
            ?.removePostfix("?", isPostfixBeginning = true)
        val nameBuilding = parser.searchOneOrNull(">", "<")
        if (linkBuilding.isNotNullOrBlank() && nameBuilding.isNotNullOrBlank()) {
            contextBuilder.building = Participant(linkBuilding, nameBuilding)
        }
        val linkRoom = parser.searchOneOrNull("href=\"/pomeschenie", "\"")
            ?.removePostfix("?", isPostfixBeginning = true)
        val nameRoom = parser.searchOneOrNull(">", "<")
        if (linkRoom.isNotNullOrBlank() && nameRoom.isNotNullOrBlank()) {
            contextBuilder.room = Participant(linkRoom, nameRoom)
        }
    }

    fun getLoadingTime(parser: SequentialParser) : Instant {
        // 2010-06-01T22:19:44.475Z
        // 30.06.2021 11:03:00
        var instant: Instant? = null
        parser.searchOneOrNull("time-updater", "</b>") { it, _ ->
            instant = it.raspUpdateToInstant()
        }
        return instant ?: RaspClock.NULL_TIME
    }
}