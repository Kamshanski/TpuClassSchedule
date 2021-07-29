package edu.kamshanski.tpuclassschedule.model.loader.converters

import com.company.sequential_parser.SearchConsumer3
import com.company.sequential_parser.SearchToken
import edu.kamshanski.tpuclassschedule.model.loader.getLinkSearchTokens
import edu.kamshanski.tpuclassschedule.model.loader.parseRaspUrl
import edu.kamshanski.tpuclassschedule.model.loader.responses.TimeReferenceResponse
import edu.kamshanski.tpuclassschedule.model.time.RaspClock
import edu.kamshanski.tpuclassschedule.utils.IllegalWeekException
import edu.kamshanski.tpuclassschedule.utils.IllegalYearException
import edu.kamshanski.tpuclassschedule.utils.collections.map
import edu.kamshanski.tpuclassschedule.utils.string.formatOfNull
import edu.kamshanski.tpuclassschedule.utils.string.sequential_parser.SequentialParser3
import okhttp3.ResponseBody
import retrofit2.Converter
import java.net.MalformedURLException
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalContracts
class TimeReferenceConverter : Converter<ResponseBody, TimeReferenceResponse> {
    override fun convert(value: ResponseBody): TimeReferenceResponse {
        val src = value.string()
        checkResponse(src)
        val rawReference = SequentialParser3(
                src,
                TimeReferenceAcceptor(),
                *getLinkSearchTokens { url, _, _, acceptor ->
                    acceptor?.apply {
                        if (link == null) {
                            link = url
                        }
                    }
                },
                SearchToken("Время", "<br />") { date, _, _, acceptor ->
                    acceptor?.date = date
                }
        ).parse()!!

        val info = parseRaspUrl(rawReference.link!!)

        val date = rawReference.date!!
        val firstDot = date.indexOf('.')
        val day = date.substring(firstDot - 2, firstDot).toInt()
        val month = date.substring(firstDot + 1, firstDot + 3).toInt()
        val year = date.substring(firstDot + 4, firstDot + 6).toInt() + 2000  // without 2000. 2021 is 21
        val instant = RaspClock.instant(year, month, day)

        return TimeReferenceResponse(info.year, info.week,instant)
    }

    private fun checkResponse(src: String)  {
        val errorMsgs = listOf("Сводный линейный график не найден", "Указана неправильная неделя сводного линейного графика", "Страница не найдена")
        val (i, error) = src.findAnyOf(errorMsgs) ?: validateResponse(src) ?: return

        when (error) {
            errorMsgs[0] -> throwUrlYearWeekException(src) { _, year ->
                throw IllegalYearException("Year %d is incorrect with week ".formatOfNull(year)) }
            errorMsgs[1] -> throwUrlYearWeekException(src) { week, year ->
                throw IllegalWeekException("Week %d is incorrect at year %d".formatOfNull(week, year)) }
            errorMsgs[2] -> throw MalformedURLException("$error. Возможно непрвильная ссылка на группу/препода/аудиторию")
        }

    }

    private fun validateResponse(src: String) : Pair<Int, String>? {
        if (src.contains("<input type=\"hidden\" value=\"https://rasp.tpu.ru/"))
            return null
        else
            throw MalformedURLException("Неизвестный ответ: \n" + src)
    }

    fun throwUrlYearWeekException(src: String, lazyError: (week: Int, year: Int) -> String) {
        val urlConsumer: SearchConsumer3<ArrayList<ArrayList<Int>>> = { value, _, _, acceptor ->
            val yearAndWeekConsumer: SearchConsumer3<ArrayList<Int>> =
                    { v, _, _, ar -> v.toIntOrNull()?.let { ar?.add(it) }}
            val yearAndWeek = SequentialParser3(
                    value,
                    ArrayList(2),   // God, nedelya
                    *getYearAndWeekSearchTokens(yearAndWeekConsumer)
            ).parse()!!
            acceptor?.add(yearAndWeek)
        }

        val list = SequentialParser3(
                src,
                ArrayList<ArrayList<Int>>(),
                *getLinkSearchTokens(urlConsumer),
        ).parse()!!
        for (p in list) {
            if (p.size == 2)
                lazyError(p[1], p[0])
        }
    }

    fun <A> getYearAndWeekSearchTokens(consumer: SearchConsumer3<A>) : Array<SearchToken<A>> {
        return arrayOf(
                "god=" to "&amp",
                "nedelya=" to "\">"
        ).map { _, markers -> SearchToken(markers.first, markers.second, consumer = consumer) }
    }


    class TimeReferenceAcceptor(var link: String? = null, var date: String? = null)

}