package edu.kamshanski.tpuclassschedule.model.loader

import android.annotation.SuppressLint
import android.util.Base64
import com.company.sequential_parser.SearchConsumer3
import com.company.sequential_parser.SearchToken
import edu.kamshanski.tpuclassschedule.BuildConfig
import edu.kamshanski.tpuclassschedule.utils.IllegalFormatException
import edu.kamshanski.tpuclassschedule.utils.collections.map
import edu.kamshanski.tpuclassschedule.utils.primitives.xor
import edu.kamshanski.tpuclassschedule.utils.string.removePostfix
import edu.kamshanski.tpuclassschedule.utils.string.removePrefix
import java.lang.StringBuilder
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime


@ExperimentalTime
@ExperimentalContracts
fun parseRaspUrl(url: String) : LinkYearWeek {
    val isRaspUrl = isRaspSourceUrl(url)

    if (!isRaspUrl)
        throw IllegalFormatException("Url $url does not contains source info")

    val splited = StringBuilder(url)
            .removePostfix(RASP_URL_MARKER, isPostfixBeginning = true)
            .removePrefix(RetrofitClient.BASE_URL, isPrefixEnding = true)
            .split("/")

    val link = splited[0]
    val year = splited[1].toInt()
    val week = splited[2].toInt()
    return LinkYearWeek(link, year, week)
}

public fun isRaspSourceUrl(url: String) = url.contains(RASP_URL_MARKER)

// этот маркер позволяет определить, что результат был получен через переадресацию
// от https://rasp.tpu.ru/redirect/kalendar.html?hash=ХХХХХХ
// к https://rasp.tpu.ru/ХХХХХХХХХХ/ХХХХ/ХХ/view.html
private const val RASP_URL_MARKER = "/view.html"

class LinkYearWeek(val link: String, val year: Int, val week: Int)

fun <A> getLinkSearchTokens(consumer: SearchConsumer3<A>) : Array<SearchToken<A>> {
    return arrayOf(
            "og:url\" content=\"https://rasp.tpu.ru/" to "\">",
            "twitter:url\" content=\"https://rasp.tpu.ru/" to "\">"
    ).map { _, markers ->
        SearchToken(markers.first, markers.second, times = SearchToken.ANY, consumer = consumer)
    }
}

fun <A> getEncryptKeySearchTokens(consumer: SearchConsumer3<A>) : SearchToken<A> =
    SearchToken("encrypt\" content=\"", "\">", consumer = consumer)


object Decryptor {
    @SuppressLint("NewApi")
    fun decode(original: String, key: String): String {
        val decoded = if (BuildConfig.DEBUG) {
            String(java.util.Base64.getDecoder().decode(original))
        } else {
            String(Base64.decode(original, Base64.DEFAULT))
        }
        return xor(decoded, key)
    }

    private fun xor(str: String, key: String): String {
        val newStr = StringBuilder()
        for (i in 0 until str.length) {
            val c = (str[i] xor key[i % key.length])
            newStr.append(c)
        }
        return newStr.toString()
    }

    fun String.raspDecode(key: String) = decode(this, key)
}