package edu.kamshanski.tpuclassschedule.model.loader

import androidx.annotation.IntRange
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.TimeReferenceResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.Source
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import edu.kamshanski.tpuclassschedule.utils.*
import edu.kamshanski.tpuclassschedule.utils.nice_classes.AccessLimiter.Companion.Limited
import edu.kamshanski.tpuclassschedule.utils.nice_classes.UnpredictedException
import edu.kamshanski.tpuclassschedule.utils.nice_classes.manyTry
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.lang.Exception
import java.net.SocketTimeoutException
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
class RaspLoadHelper(val service: RaspApiService) {

    suspend fun getSources(searchInput: String) : List<ShortSource> {
        val sourcesResult = service.getSources(searchInput)
        return sourcesResult.result
    }

    suspend fun getSourceInfo(source: ShortSource) : Source {
        val loadedSource = try {
            val hash = source.hash
            val response = service.getSourceInfo(hash)
            Source(hash, source.name, response.link, response.info)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw LoadingException("Error while loading source $source", ex)
        }

        return loadedSource
    }

    // TODO: перенести в подходящее место и правильно реализовать в коде
    val referenceLinks = arrayOf("pomeschenie_44", "pomeschenie_64")

    // Бесполезно, потому что текущий год подгружается вместе SourceInfo
    suspend fun getCurrentYear() : Int {
        val exCallback :(Class<out Throwable>) -> Unit = { lg(it) }
        val year = manyTry(arrayListOf(
                Limited(SocketTimeoutException::class.java, 2, exCallback),
                Limited(InterruptedIOException::class.java, 2, exCallback),
                Limited(HttpException::class.java, 2, exCallback),
        )) {
            service.getCurrentYear()
        } ?: throw UnknownError()
        return year
    }

    // Окольный путь, потому что rasp.tpu не принимает /1/, а только /01/
    private suspend fun getYearTimeRange(
            link: String,
            @IntRange(from = 2003, to = 2099)year: Int,
            @IntRange(from = 1, to = 52) week: Int
    ) : TimeReferenceResponse = service.getYearTimeRange(link, year.toString(), "%02d".format(week))

    suspend fun getYearTimeRange(year: Int) : YearTimeRange {
        val link = referenceLinks[0]
        val exCallback: (Class<out Throwable>) -> Unit = { lg(it) }

        // TODO:  Лучше сделать отдельный класс для хранения кучи Limited(SocketTimeoutException::class.java, 2) с общим для всех callback'ом
        // TODO:  Сделать infix fun atMost вместо Limited
        // TODO:  Сделать Mutable и не Mutable interface для CountLimitedAccessor, чтобы можно было иметь обнуляемые счётчики
        val beginReference = manyTry(arrayListOf(
                Limited(SocketTimeoutException::class.java, 2, exCallback),
                Limited(InterruptedIOException::class.java, 2, exCallback),
                Limited(HttpException::class.java, 2, exCallback),
        )) {
            getYearTimeRange(link, year, week = 1)
        } ?: throw UnpredictedException()

        // TODO: Больше 52 не бывает. Установить на 52
        var lastWeekNum = 53
        val endReference = manyTry(arrayListOf(
                Limited(IllegalWeekException::class.java, 3) { lastWeekNum--; lg(it) },
                Limited(SocketTimeoutException::class.java, 2, exCallback),
                Limited(InterruptedIOException::class.java, 2, exCallback),
                Limited(HttpException::class.java, 2) { lastWeekNum--; lg(it)},
        )) {
            getYearTimeRange(link, year, week = lastWeekNum)
        } ?: throw UnpredictedException()

        // не знаю, зачем. пусть будет
        check(endReference.year == beginReference.year) {
            throw SamePropertyValueConflictException("TimeReferenceResponse.year", endReference.year, beginReference.year, "==") }

        return YearTimeRange(endReference.year, endReference.week, beginReference.date, endReference.date)
    }

}