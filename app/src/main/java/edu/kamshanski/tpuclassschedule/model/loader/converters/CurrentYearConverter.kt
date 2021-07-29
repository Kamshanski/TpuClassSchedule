package edu.kamshanski.tpuclassschedule.model.loader.converters

import com.company.sequential_parser.SearchToken
import edu.kamshanski.tpuclassschedule.utils.string.sequential_parser.SequentialParser3
import okhttp3.ResponseBody
import retrofit2.Converter
import java.util.concurrent.atomic.AtomicInteger

class CurrentYearConverter : Converter<ResponseBody, Int> {
    override fun convert(value: ResponseBody?): Int {
        value!!
        val year = SequentialParser3(
                value.string(),
                AtomicInteger(0),
                SearchToken(" / 20", " учебный год") { maybeYear, _, _, acceptor ->
                    try {
                        val year = maybeYear.toInt() + 2000 - 1
                        acceptor!!.set(year)
                    } catch (nfe: NumberFormatException) {
                        nfe.printStackTrace()
                    }
                }
        ).parse()!!

        return year.get()
    }
}