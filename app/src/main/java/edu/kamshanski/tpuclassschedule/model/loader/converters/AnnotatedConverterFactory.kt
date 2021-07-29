package edu.kamshanski.tpuclassschedule.model.loader.converters

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourcesResponse
import edu.kamshanski.tpuclassschedule.model.preferences.CommonPreferences
import edu.kamshanski.tpuclassschedule.utils.lg
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@ExperimentalContracts
class AnnotatedConverterFactory private constructor(val context: Context): Converter.Factory()  {
    companion object {
        @JvmStatic fun create(context: Context) = AnnotatedConverterFactory(context)
    }
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        for (an in annotations) {
            if (an is edu.kamshanski.tpuclassschedule.model.loader.converters.Converter) {
                val token = object : TypeToken<SourcesResponse>() {}
                val myType = token.type
                val raw = token.rawType
                if (myType == type) {
                    lg("Types are equal and are $myType")
                }
                if (raw == type) {
                    lg("Types are equal and are $raw")
                }

                val actualConverter = when(an.converterClass) {
                    SourcesListConverter::class -> {
                        val gson = GsonBuilder()
                                .registerTypeAdapter(myType, SourcesListConverter.SourceListAdapter())
                                .create()
                        val factory = GsonConverterFactory.create(gson)
                        val converter = factory.responseBodyConverter(type, annotations, retrofit)!!
                        converter
                    }
                    WeekScheduleConverter::class -> WeekScheduleConverter(
                        CommonPreferences(context).yearsTimeRanges)
                    CurrentYearConverter::class -> CurrentYearConverter()
                    SourceConverter::class -> SourceConverter()
                    TimeReferenceConverter::class -> TimeReferenceConverter()
                    else -> return null
                }
                return actualConverter
            }
        }
        return null
    }
}
