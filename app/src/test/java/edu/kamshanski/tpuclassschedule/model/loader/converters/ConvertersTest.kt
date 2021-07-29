package edu.kamshanski.tpuclassschedule.model.loader.converters

import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.WeekScheduleResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.Activity
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.Context
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.Day
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.Window
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.OtherInfo
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule.actors.Participant
import edu.kamshanski.tpuclassschedule.test_files.RaspFilesDb
import edu.kamshanski.tpuclassschedule.test_files.TestRaspClock
import edu.kamshanski.tpuclassschedule.test_utils.assertEqualContent
import edu.kamshanski.tpuclassschedule.test_utils.sameContent
import edu.kamshanski.tpuclassschedule.utils.function_extensions.withBoth
import edu.kamshanski.tpuclassschedule.utils.lg
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalContracts
class ConvertersTest{
    @TestFactory
    fun `Source Response parsing`() : Collection<DynamicTest> {
        val sourceConverter = SourceConverter()
        val tests = RaspFilesDb.raspFiles.asSequence()
            .filter { it.sourceResponse != null }
            .map { file ->
                DynamicTest.dynamicTest(file.fileName) {
                    val result = sourceConverter.convert(file.asResponseBody)
                    assertSourceResponse(result, file.sourceResponse!!)
                    println("Responce Test: $file.fileName")
                    println("   Expected: ${file.sourceResponse}")
                    println("   Actual:   ${result}")
                }
            }.toList()

        return tests
    }

    fun assertSourceResponse(result: SourceResponse, ans: SourceResponse) {
        assertEquals(result.link, ans.link)
        assertEquals(result.info.fullName, ans.info.fullName)
        assertEquals(result.info.nameAppendix, ans.info.nameAppendix)
        assertEqualContent(result.info.otherInfo, ans.info.otherInfo)  { o1, o2 ->
            o1.link == o2.link && o1.info == o2.info
        }
    }

    @TestFactory
    fun `Week Schedule parsing`() : Collection<DynamicTest> {
        val tests = RaspFilesDb.raspFiles.asSequence()
            .filter { it.weekScheduleResponse != null }
            .map { file ->
                DynamicTest.dynamicTest(file.fileName) {
                    val sourceConverter = WeekScheduleConverter(TestRaspClock.timeRanges)
                    val result = sourceConverter.convert(file.asResponseBody)
                    assertWeekScheduleResponse(file.weekScheduleResponse!!, result)
                    println("Week Schedule Test: ${file.fileName}")
                    println("   Expected: ${file.weekScheduleResponse}")
                    println("   Actual:   ${result}")
                }
            }.toList()
        return tests
    }

    val participantMatcher = { p1: Participant?, p2: Participant? ->
        p1?.name == p2?.name && p1?.link == p2?.link
    }

    val conextsMatcher = { c1: Context, c2: Context ->
                participantMatcher(c1.building, c2.building) &&
                participantMatcher(c1.room, c2.room) &&
                sameContent(c1.teachers, c2. teachers, participantMatcher) &&
                sameContent(c1.groups, c2. groups, participantMatcher)
    }

    val activitiesMatcher = { a1: Activity, a2: Activity ->
                a1.name     == a2.name        &&
                a1.type     == a2.type        &&
                a1.link     == a2.link        &&
                a1.fullName == a2.fullName     &&
                sameContent(a1.contexts, a2.contexts, conextsMatcher)
    }

    val windowsMatcher = { w1: Window, w2: Window ->
                w1.ordinal  == w2.ordinal &&
                w1.from     == w2.from &&
                w1.till     == w2.till &&
                sameContent(w1.activities, w2.activities, activitiesMatcher)
    }

    val daysMatcher = { d1: Day, d2: Day ->
                d1.date         == d2.date &&
                d1.dayOfWeek    == d2.dayOfWeek &&
                d1.dayRoutine   == d2.dayRoutine &&
                sameContent(d1.windows, d2.windows, windowsMatcher)
    }

    val otherInfoMatcher = {o1: OtherInfo, o2: OtherInfo ->
        o1.link == o2.link && o1.info == o2.info
    }

    fun assertWeekScheduleResponse(expected: WeekScheduleResponse, actual: WeekScheduleResponse) {
        assertEquals(expected.updateTime, actual.updateTime)
        assertEqualContent(expected.scheduleMeta, actual.scheduleMeta, otherInfoMatcher)
        withBoth(expected.sourceInfo, actual.sourceInfo) { e, a ->
            assertEquals(e.fullName, a.fullName)
            assertEquals(e.nameAppendix, a.nameAppendix)
            assertEqualContent(e.otherInfo, a.otherInfo, otherInfoMatcher)
        }
        withBoth(expected.schedule, actual.schedule)  { e, a ->
            assertEquals(e.week, a.week)
            assertEquals(e.isOdd, a.isOdd)
            assertEquals(e.startDate, a.startDate)
            assertEqualContent(e.days, a.days, daysMatcher)
        }
    }



    companion object {
        @JvmStatic
        @BeforeAll
        fun setup() {
            mockkStatic(::lg)
            every { lg(any()) } returns Unit
            println("Preparing data for tests")
            RaspFilesDb.raspFiles.size
            TestRaspClock.timeRanges.size
            TestRaspClock.windowTimesPeriodsNew.size
            TestRaspClock.windowTimesPeriodsOld.size
            println("Data was successfully prepared")
        }
    }

//    class OtherInfoListMatcher(list: List<OtherInfo>) : BaseMatcher<List<OtherInfo>>() {
//        override fun describeTo(description: Description) {
//            description.appendText("Other info list is the same")
//        }
//
//        override fun matches(item: Any?): Boolean {
//
//        }
//
//    }
}