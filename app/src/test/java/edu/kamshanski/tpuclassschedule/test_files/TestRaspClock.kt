package edu.kamshanski.tpuclassschedule.test_files

import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.model.time.RaspClock.dmyToInstant
import edu.kamshanski.tpuclassschedule.model.time.RaspClock.period
import kotlinx.datetime.DateTimePeriod

object TestRaspClock {
    val timeRanges = setOf(
        YearTimeRange(2003, 52, "01.09.03".dmyToInstant(), "23.08.04".dmyToInstant()),
        YearTimeRange(2004, 52, "30.08.04".dmyToInstant(), "22.08.05".dmyToInstant()),
        YearTimeRange(2005, 52, "29.08.05".dmyToInstant(), "21.08.06".dmyToInstant()),
        YearTimeRange(2006, 52, "28.08.06".dmyToInstant(), "20.08.07".dmyToInstant()),
        YearTimeRange(2007, 52, "03.09.07".dmyToInstant(), "25.08.08".dmyToInstant()),
        YearTimeRange(2008, 52, "01.09.08".dmyToInstant(), "24.08.09".dmyToInstant()),
        YearTimeRange(2009, 52, "31.08.09".dmyToInstant(), "23.08.10".dmyToInstant()),
        YearTimeRange(2010, 52, "30.08.10".dmyToInstant(), "22.08.11".dmyToInstant()),
        YearTimeRange(2011, 52, "29.08.11".dmyToInstant(), "20.08.12".dmyToInstant()),
        YearTimeRange(2012, 51, "27.08.12".dmyToInstant(), "12.08.13".dmyToInstant()),
        YearTimeRange(2013, 52, "02.09.13".dmyToInstant(), "25.08.14".dmyToInstant()),
        YearTimeRange(2014, 52, "01.09.14".dmyToInstant(), "24.08.15".dmyToInstant()),
        YearTimeRange(2015, 52, "31.08.15".dmyToInstant(), "22.08.16".dmyToInstant()),
        YearTimeRange(2016, 52, "29.08.16".dmyToInstant(), "21.08.17".dmyToInstant()),
        YearTimeRange(2017, 52, "28.08.17".dmyToInstant(), "20.08.18".dmyToInstant()),
        YearTimeRange(2018, 52, "27.08.18".dmyToInstant(), "19.08.19".dmyToInstant()),
        YearTimeRange(2019, 51, "02.09.19".dmyToInstant(), "17.08.20".dmyToInstant()),
        YearTimeRange(2020, 52, "31.08.20".dmyToInstant(), "23.08.21".dmyToInstant()),
        YearTimeRange(2021, 52, "30.08.21".dmyToInstant(), "22.08.22".dmyToInstant()),
    )
    public operator fun get(year: Int) : YearTimeRange {
        return timeRanges.first { it.year == year}
    }

    val windowTimesPeriodsNew = listOf(
        period(hours = 8, minutes = 30) to period(hours = 10, minutes = 5),
        period(hours = 10, minutes = 25) to period(hours = 12, minutes = 0),
        period(hours = 12, minutes = 40) to period(hours = 14, minutes = 15),
        period(hours = 14, minutes = 35) to period(hours = 16, minutes = 10),
        period(hours = 16, minutes = 30) to period(hours = 18, minutes = 5),
        period(hours = 18, minutes = 25) to period(hours = 20, minutes = 0),
        period(hours = 20, minutes = 20) to period(hours = 21, minutes = 55),
    )

    val windowTimesPeriodsOld = listOf(
        period(hours = 8, minutes = 30) to period(hours = 10, minutes = 5),
        period(hours = 10, minutes = 25) to period(hours = 12, minutes = 0),
        period(hours = 12, minutes = 20) to period(hours = 13, minutes = 55),
        period(hours = 14, minutes = 15) to period(hours = 15, minutes = 50),
        period(hours = 16, minutes = 10) to period(hours = 17, minutes = 45),
        period(hours = 18, minutes = 5) to period(hours = 19, minutes = 40),
        period(hours = 20, minutes = 0) to period(hours = 21, minutes = 35),
    )

    fun winStart(@IntRange(from = 0, to = 6) ordinal: Int, @IntRange(from = 2003, to = 2099)year: Int) : DateTimePeriod {
        return if (year < 2020)
            windowTimesPeriodsOld[ordinal].first
        else
            windowTimesPeriodsNew[ordinal].first

    }
    fun winEnd(@IntRange(from = 0, to = 6) ordinal: Int, year: Int) : DateTimePeriod {
        return if (year < 2020)
            windowTimesPeriodsOld[ordinal].second
        else
            windowTimesPeriodsNew[ordinal].second
    }
}