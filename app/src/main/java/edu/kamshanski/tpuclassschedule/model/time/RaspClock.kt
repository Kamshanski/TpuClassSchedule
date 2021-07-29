package edu.kamshanski.tpuclassschedule.model.time

import androidx.annotation.InspectableProperty
import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.utils.primitives.toFormatString
import kotlinx.datetime.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

object RaspClock {
    val NULL_TIME = java.time.Instant.EPOCH.toKotlinInstant()
    val TOMSK_ZONE = TimeZone.of("UTC+7")
    const val TOMSK_TIMEZONE = "+07:00"
    const val INSTANT_TOMSK_ENDING = "T00:00:00.000$TOMSK_TIMEZONE"

    val raspDateTimeRegex
        get() = Regex("(\\d{2})\\.(\\d{2})\\.(\\d{4})\\s(\\d{2}):(\\d{2}):(\\d{2})")

    fun instant(year: Int, month: Int, day: Int) : Instant {
        return Instant.parse("$year-${month.toFormatString(2)}-${day.toFormatString(2)}$INSTANT_TOMSK_ENDING")
    }



    // -------- date utils

    @ExperimentalTime
    fun getDateOf(year: YearTimeRange,
                  @IntRange(from = 1, to = 52) week: Int,
                  day: DayOfWeek) : Instant {
        checkWeekNumber(week)
        val period = duration(week - 1, day.ordinal)
        return year.firstDate.plus(period)
    }

    @ExperimentalTime
    private fun duration(
        @IntRange(from = 0, to = 52) weeks: Int,
        @IntRange(from = 0, to = 6)days: Int) : Duration {
        return (weeks * 7 + days).toDuration(DurationUnit.DAYS)
    }

    public fun String.dmyToInstant(delimiter: String = ".", zone: String = TOMSK_TIMEZONE) : Instant {
        val splited = split(delimiter)
        return "20${splited[2]}-${splited[1]}-${splited[0]}$INSTANT_TOMSK_ENDING".toInstant()
    }

    public fun String.raspUpdateToInstant() : Instant? {
        val destructured = raspDateTimeRegex.find(this)?.destructured
        if (destructured != null) {
            val (d, M, y, h, m, s) = destructured
            return "$y-$M-${d}T$h:$m:$s.000$TOMSK_TIMEZONE".toInstant()
        }
        return null
    }

    @JvmStatic public fun period( years: Int = 0,
                                  months: Int = 0,
                                  days: Int = 0,
                                  hours: Int = 0,
                                  minutes: Int = 0,
                                  seconds: Int = 0,
                                  nanoseconds: Long = 0
    ) = DateTimePeriod(years, months, days, hours, minutes, seconds, nanoseconds)

    /**
     * 0 - MONDAY,
     * 1 - TUESDAY
     * ...
     * 6 - SUNDAY
     * @param dayNumber - days to plus to Monday
     * @return [DayOfWeek]
     */
    @JvmStatic fun dayOfWeek(@IntRange(from = 0, to = 6) dayNumber: Int) = DayOfWeek.MONDAY + dayNumber.toLong()

    // ----------------- checks

    // TODO: когда-нибудь заменить это аннотациями
    @JvmStatic fun checkWeekNumber(week: Int) {
        checkWeekNumber(week, 52)
    }
    @JvmStatic fun checkWeekNumber(week: Int, lastWeekNum: Int) {
        if (week !in 1..lastWeekNum) {
            throw IllegalArgumentException("Week number must be in range [1, 52]. Entered: $week")
        }
    }
    @JvmStatic fun checkYearNumber(year: Int) {
        if (year !in 2003..2099) {
            throw IllegalArgumentException("Week number must be in range [2003, 2099]. Entered: $year")
        }
    }
    @JvmStatic fun checkNotSunday(dayOfWeek: DayOfWeek) {
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            throw IllegalArgumentException("DayOfWeek mustn't be SUNDAY")
        }
    }
    @JvmStatic fun checkClassNumber(classNumber: Int) {
        if (classNumber !in 0..6) {
            throw IllegalArgumentException("DayOfWeek mustn't be SUNDAY")
        }
    }
}

