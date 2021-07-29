package edu.kamshanski.tpuclassschedule.model.loader.converters

import com.google.gson.*
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourcesResponse
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type

/**
 * Response is actually a JSON, but only some of fields are necessary. So, it's faster to skip most of them
 */
// fake converter that just allows to determine actual converter by annotation
abstract class SourcesListConverter : Converter<ResponseBody, SourcesResponse> {
    class SourceListAdapter : JsonDeserializer<SourcesResponse> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SourcesResponse {
            val j = json.asJsonObject
            val jsonArray = j["result"].asJsonArray
            val array = ArrayList<ShortSource>()
            for (elem in jsonArray) {
                val name = elem.asJsonObject["text"].asString
                if (name.isNullOrBlank()) continue
                if (validateSource(name)) continue

                val hash = elem.asJsonObject["url"].asString?.substringAfter("/kalendar.html?hash=", missingDelimiterValue = "")
                if (hash.isNullOrBlank()) continue
                val fullName = elem.asJsonObject["html"].asString?.replace("<span class=\"text-muted\">", "")?.replace("</span>", "")
                if (fullName.isNullOrBlank()) continue

                array += ShortSource(hash, name, fullName)
            }

            return SourcesResponse(array)
        }

        // TODO: в новом учебном году настроить это ещё раз
        private fun validateSource(name: String) : Boolean {
            val spacesCount = name.count {it == ' '}
            return spacesCount == 0
        }
    }
}
