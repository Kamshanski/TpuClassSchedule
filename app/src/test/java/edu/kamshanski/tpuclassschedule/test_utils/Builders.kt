package edu.kamshanski.tpuclassschedule.test_utils

import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.model.loader.responses.WeekScheduleResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.ActivityType
import edu.kamshanski.tpuclassschedule.model.rasp_entities.constants.DayRoutine
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.*
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.OtherInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.ParticipantInfo
import edu.kamshanski.tpuclassschedule.model.time.RaspClock.dmyToInstant
import edu.kamshanski.tpuclassschedule.model.time.RaspClock.raspUpdateToInstant
import edu.kamshanski.tpuclassschedule.test_files.TestRaspClock.winEnd
import edu.kamshanski.tpuclassschedule.test_files.TestRaspClock.winStart
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import org.junit.jupiter.api.fail

class TestWeekScheduleResponse (
    val year: Int,
    val updateTime: String,
    val sundayDate: String,
    val sourceInfo: TestParticipantInfo,
    scheduleMeta: Map<String, String> = emptyMap(),
    val testWeekSchedule: TestWeekSchedule,
    storedActivities: Map<String, String> = emptyMap(),
    storedGroups: Map<String, String> = emptyMap(),
    storedTeachers: Map<String, String> = emptyMap(),
    storedPlaces: Map<Pair<String, String>, Map<String, String>> = emptyMap(),
) {
    val _storedActivities: Map<String,String>
    val _storedGroups: List<TestParticipant>
    val _storedTeachers: List<TestParticipant>
    val _storedPlaces: Map<TestParticipant, List<TestParticipant>>
    val _scheduleMeta: List<OtherInfo>

    init {
        val sa = storedActivities.map { (k, v) -> k.trim() to v.trim() }
        _storedActivities = mapOf(*sa.toTypedArray())

        _storedGroups = storedGroups.map { (k, v) -> TestParticipant(k.trim(), v.trim()) }
        _storedTeachers = storedTeachers.map { (k, v) -> TestParticipant(k.trim(), v.trim()) }
        val sp = storedPlaces.map { (k, v) -> 
            TestParticipant(k.first.trim(), k.second.trim()) to v.map { (kk, vv) -> 
                TestParticipant(kk.trim(), vv.trim()) 
            } 
        }
        _storedPlaces = mapOf(*sp.toTypedArray())
        _scheduleMeta = scheduleMeta.map { (k, v) -> OtherInfo(k.trim(), v.trim()) }
    }

    fun build() : WeekScheduleResponse {
        return WeekScheduleResponse(
            updateTime = updateTime.trim().raspUpdateToInstant()!!,
            loadingTime = Clock.System.now(),
            sourceInfo = sourceInfo.build(),
            scheduleMeta = _scheduleMeta,
            schedule = testWeekSchedule.build(
                year, sundayDate, _storedActivities, _storedGroups, _storedTeachers, _storedPlaces
            )
        )
    }

    class TestWeekSchedule (
        val week: Int,
        val startDate: String,
        val testDays: MutableList<TestDay> = mutableListOf()
    ) {
        fun build(
            year: Int,
            sundayDate: String,
            storedActivities: Map<String, String>,
            storedGroups: List<TestParticipant>,
            storedTeachers: List<TestParticipant>,
            storedPlaces: Map<TestParticipant, List<TestParticipant>>) : WeekSchedule {
            return WeekSchedule(
                week = week,
                startDate = startDate.trim().dmyToInstant(),
                days = testDays
                    .also{
                        it.add(TestDay.getSunday(sundayDate))
                    }.map { it.build(
                        year, week, storedActivities, storedGroups, storedTeachers, storedPlaces
                    ) })
        }
    }

    class TestDay(
        val date: String,
        val dayOfWeek: DayOfWeek,
        var dayRoutine: DayRoutine = DayRoutine.WORKING,
        val testWindows: List<TestWindow> = emptyList()
    ) {
        constructor(
            date: String,
            dayOfWeek: String,
            dayRoutine: DayRoutine = DayRoutine.FREE,
            testWindows: List<TestWindow> = emptyList()
        ) : this(
            date = date,
            dayRoutine = dayRoutine,
            testWindows = testWindows,
            dayOfWeek = dayOfWeek.let {
                return@let when(dayOfWeek.trim()) {
                    "понедельник" -> DayOfWeek.MONDAY
                    "вторник" -> DayOfWeek.TUESDAY
                    "среда" -> DayOfWeek.WEDNESDAY
                    "четверг" -> DayOfWeek.THURSDAY
                    "пятница" -> DayOfWeek.FRIDAY
                    "суббота" -> DayOfWeek.SATURDAY
                    "воскресенье" -> DayOfWeek.SUNDAY
                    else -> fail("dayOfWeek: $dayOfWeek")} }
        )


        fun build(
            year: Int,
            week: Int,
            storedActivities: Map<String, String>,
            storedGroups: List<TestParticipant>,
            storedTeachers: List<TestParticipant>,
            storedPlaces: Map<TestParticipant, List<TestParticipant>>,) : Day {
            if (dayRoutine != DayRoutine.HOLIDAY) {
                dayRoutine =
                    if (dayOfWeek == DayOfWeek.SUNDAY) {
                        DayRoutine.HOLIDAY
                    } else if (testWindows.isEmpty()) {
                        DayRoutine.FREE
                    } else {
                        DayRoutine.WORKING
                    }
            }

            return Day(
                date = date.trim().dmyToInstant(),
                dayOfWeek = dayOfWeek,
                dayRoutine = dayRoutine,
                windows = testWindows.map {
                    it.build(
                        year, week, storedActivities, storedGroups, storedTeachers, storedPlaces)})
        }

        companion object {
            @JvmStatic fun getSunday(date: String) : TestDay {
                return TestDay(date, DayOfWeek.SUNDAY, DayRoutine.HOLIDAY)
            }
        }
    }

    class TestWindow (
        @IntRange(from = 0, to = 6) val ordinal: Int,
        val testActivities: List<TestActivity>
    ) {
        fun build(
            year: Int,
            week: Int,
            storedActivities: Map<String, String>,
            storedGroups: List<TestParticipant>,
            storedTeachers: List<TestParticipant>,
            storedPlaces: Map<TestParticipant, List<TestParticipant>>,
        ) : Window{
            return Window(
                from = winStart(ordinal, year),
                till = winEnd(ordinal, year),
                ordinal = ordinal,
                activities = testActivities.map { it.build(
                    year, week, storedActivities, storedGroups, storedTeachers, storedPlaces
                ) }
            )
        }
    }

    class TestActivity (
        val name: String,
        val type: String,
        val link: String = "",
        val testContext: List<TestContext> = emptyList()
    ) {
        fun build(
            year: Int,
            week: Int,
            storedActivities: Map<String, String>,
            storedGroups: List<TestParticipant>,
            storedTeachers: List<TestParticipant>,
            storedPlaces: Map<TestParticipant, List<TestParticipant>>,
        ) : Activity {
            val fullName = storedActivities[name.trim()]
            if (fullName == null) {
                fullName!!
            }
            val typeAsObj = ActivityType.getByAcronym(type.trim())
            if (typeAsObj == null) {
                typeAsObj!!
            }
            return Activity(name.trim(), fullName, typeAsObj, link.trim(), testContext.map { it.build(
                year, week, storedGroups, storedTeachers, storedPlaces
            ) })
        }
    }

    class TestContext(
        val groups: List<String> = emptyList(),
        val teachers: List<String> = emptyList(),
        val building: String? = null,
        val room: String? = null
    ) {
        fun build(
            year: Int,
            week: Int,
            storedGroups: List<TestParticipant>,
            storedTeachers: List<TestParticipant>,
            storedPlaces: Map<TestParticipant, List<TestParticipant>>,
        ) : Context {
            fun getTestParticipant(iter: Iterable<TestParticipant>, name: String) : TestParticipant {
                val found = iter.firstOrNull { it.name == name.trim() }
                if (found == null) {
                    found!!
                }
                return found
            }
            val tb = if (building != null) {
                getTestParticipant(storedPlaces.keys, building.trim())
            } else null

            val b = tb?.build(year, week)

            val r = if (room != null && tb != null) {
                getTestParticipant(storedPlaces[tb]!!, room.trim()).build(year, week)
            } else null

            val g = groups.map { key ->
                getTestParticipant(storedGroups, key.trim()).build(year, week)
            }

            val t = teachers.map { key ->
                getTestParticipant(storedTeachers, key.trim()).build(year, week)
            }

            return Context (g, t, b, r
            )
        }
    }

    class TestParticipant(
        val name: String,
        val simpleLink: String,
    ) {
        fun build(
            year: Int,
            week: Int,
        ) : Participant {
            return Participant(
                name = name.trim(),
                link = "/${simpleLink.trim()}/$year/$week/view.html"
            )
        }
    }

    class TestParticipantInfo(
        val fullName: String,
        val nameAppendix: String = "",
        val otherInfo: Map<String, String> = emptyMap()
    ) {
        fun build() : ParticipantInfo {
            return ParticipantInfo(fullName, nameAppendix, otherInfo.map { (n, l) -> OtherInfo(n, l) })
        }
    }
}

