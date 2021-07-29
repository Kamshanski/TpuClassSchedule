package edu.kamshanski.tpuclassschedule._non_test_experiments_field

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.TimeUnit


//
//import android.util.Base64
//import edu.kamshanski.tpuclassschedule.model.loader.converters.SourceConverterTests
//import edu.kamshanski.tpuclassschedule.test_utils.ConverterCtg
//import org.junit.experimental.categories.Categories
//import org.junit.jupiter.api.extension.ExtendWith
//import org.mockito.Mockito.*
//import org.mockito.junit.MockitoJUnit
//import org.powermock.api.mockito.PowerMockito.`when`
//import org.powermock.api.mockito.PowerMockito.mockStatic
//import kotlin.contracts.ExperimentalContracts
//import kotlin.time.ExperimentalTime
// https://github.com/mannodermaus/android-junit5
/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Book(val number: Int) {val title = "b${number}"}
class Author(val age: Int) { val name = "a${age}" }


//Accept                    application/json, text/javascript, */*; q=0.01
//Accept-Encoding           gzip, deflate, br
//Accept-Language           ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7
//Connection                keep-alive
//Content-Length            36
//Content-Type              application/x-www-form-urlencoded; charset=UTF-8
//Cookie                    _ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077 T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; PHPSESSID=c1n5uq4djr1ecbf1g0f496rtun; _ym_isad=2; _ym_visorc=w
//Host                      rasp.tpu.ru
//Origin                    https://rasp.tpu.ru
//Referer                   https://rasp.tpu.ru/gruppa_37032/2020/6/view.html
//sec-ch-ua                 "Opera";v="77", "Chromium";v="91", ";Not A Brand";v="99"
//sec-ch-ua-mobile          ?0
//Sec-Fetch-Dest            empty
//Sec-Fetch-Mode            cors
//Sec-Fetch-Site            same-origin
//User-Agent                Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 OPR/77.0.4054.203
//X-CSRF-Token              HX6Mphy-BzXeR1bM8C3twkEH0x6dUfW-pzqCh0R2H4lID7rKc-pAGOojL5rIXt36IjCKW9Y1zezDXOPrNVtQ5g==
//X-Requested-With          XMLHttpRequest


//Host                          rasp.tpu.ru
//Connection                    keep-alive
//Cache-Control                 max-age=0
//sec-ch-ua                     "Opera";v="77", "Chromium";v="91", ";Not A Brand";v="99"
//sec-ch-ua-mobile              ?0
//Upgrade-Insecure-Requests     1
//User-Agent                    Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 OPR/77.0.4054.203
//Accept                        text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9
//Sec-Fetch-Site                same-origin
//Sec-Fetch-Mode                navigate
//Sec-Fetch-User                ?1
//Sec-Fetch-Dest                document
//Referer                       https://rasp.tpu.ru/gruppa_32710/2020/35/view.html
//Accept-Encoding               gzip, deflate, br
//Accept-Language               ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7
//Cookie                        _ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077:T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; _ym_isad=2; PHPSESSID=v2a61p2qfka3gaugns0kcu2qfs; _ym_visorc=w

class MainTest {
    @Test
    fun main() {
        val client = OkHttpClient.Builder()
            .connectionPool(ConnectionPool(1, 1, TimeUnit.HOURS))
            .build()

        val sessionId = "o41u8q7ncnga493bt6v47qs456"
        val raspUrl = "https://rasp.tpu.ru/gruppa_32710/2020/34/view.html"
        val requestView = Request.Builder()
            .get()
            .url(raspUrl)
                .addHeader("Connection      ".trim(), "keep-alive")
//                .addHeader("Host                     ".trim(), "rasp.tpu.ru")
//                .addHeader("Connection               ".trim(), "keep-alive")
//                .addHeader("Cache-Control            ".trim(), "max-age=0")
//                .addHeader("sec-ch-ua                ".trim(), "\"Opera\";v=\"77\", \"Chromium\";v=\"91\", \";Not A Brand\";v=\"99\"")
//                .addHeader("sec-ch-ua-mobile         ".trim(), "?0")
//                .addHeader("Upgrade-Insecure-Requests".trim(), "1")
//                .addHeader("User-Agent               ".trim(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 OPR/77.0.4054.203")
//                .addHeader("Accept                   ".trim(), "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
//                .addHeader("Sec-Fetch-Site           ".trim(), "same-origin")
//                .addHeader("Sec-Fetch-Mode           ".trim(), "navigate")
//                .addHeader("Sec-Fetch-User           ".trim(), "?1")
//                .addHeader("Sec-Fetch-Dest           ".trim(), "document")
//                .addHeader("Referer                  ".trim(), "https://rasp.tpu.ru/gruppa_32710/2020/34/view.html")
//                .addHeader("Accept-Encoding          ".trim(), "gzip, deflate, br")
//                .addHeader("Accept-Language          ".trim(), "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7")
//                .addHeader("Cookie                   ".trim(), "_ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077:T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; _ym_isad=2; PHPSESSID=$sessionId; _ym_visorc=w")
            .build()
        val viewCall = client.newCall(requestView)
        val viewResponse = viewCall.execute()
        val body = viewResponse.body!!.string()
        var t1 = "<meta name=\"csrf-token\" content=\"";
        var i1 = body.indexOf(t1) + t1.length;
        var t2 = "\">";
        var i2 = body.indexOf(t2, i1);
        var csfr = body.substring(i1, i2);

        val values = viewResponse.headers.values("Set-Cookie")
        val php = values.find { it[0] == 'P' }!!.let {
            it.substring(0, it.indexOf(";")+1)
        }
        val csrf = values.find { it[0] == '_' }!!.let {
            it.substring(0, it.indexOf(";"))
        }
        val cookie = "$php $csrf"


        t1 = "<meta name=\"encrypt\" content=\"";
        i1 = body.indexOf(t1) + t1.length;
        t2 = "\">";
        i2 = body.indexOf(t2, i1);
        var payload = body.substring(i1, i2);
        payload = payload.replace("=", "%3D")
        val json = "{\"content\":\"${payload}\"}"
        val keyReqBody: RequestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .method("POST", keyReqBody)
            .url("https://rasp.tpu.ru/data/encrypt/decrypt.html")
            .addHeader("Accept          ".trim(), "application/json, text/javascript, */*; q=0.01")
            .addHeader("Accept-Encoding ".trim(), "gzip, deflate, br")
//            .addHeader("Accept-Language ".trim(), "ru-RU,ru;q=0.9")
            .addHeader("Connection      ".trim(), "keep-alive")
//            .addHeader("Content-Length  ".trim(), "36")
            .addHeader("Content-Type    ".trim(), "application/x-www-form-urlencoded; charset=UTF-8")
            .addHeader("Cookie          ".trim(), cookie)
//            .addHeader("Host            ".trim(), "rasp.tpu.ru")
//            .addHeader("Origin          ".trim(), "https://rasp.tpu.ru")
            .addHeader("Referer         ".trim(), raspUrl)
//            .addHeader("sec-ch-ua       ".trim(), "\"Opera\";v=\"77\", \"Chromium\";v=\"91\", \";Not A Brand\";v=\"99\"")
//            .addHeader("sec-ch-ua-mobile".trim(), "?0")
//            .addHeader("Sec-Fetch-Dest  ".trim(), "empty")
//            .addHeader("Sec-Fetch-Mode  ".trim(), "cors")
//            .addHeader("Sec-Fetch-Site  ".trim(), "same-origin")
//            .addHeader("User-Agent      ".trim(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36 OPR/77.0.4054.203")
            .addHeader("X-CSRF-Token    ".trim(), csfr)
            .addHeader("X-Requested-With".trim(), "XMLHttpRequest")

        val call = client.newCall(request.build())
        val resp = call.execute()
        val b = resp.body!!.string()

        println(b)

    }
}
//_ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077:T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; _ym_isad=2; PHPSESSID=o41u8q7ncnga493bt6v47qs456; _ym_visorc=w

//_ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077:T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; _ym_isad=2; PHPSESSID=v2a61p2qfka3gaugns0kcu2qfs; _ym_visorc=w
//_ym_uid=1611497509749566315; _ga=GA1.2.1146702961.1611538338; __gads=ID=4096ba9ea5c11be1-2226eb57cab90077:T=1611543016:RT=1611543016:S=ALNI_MYCivdNtvLbkG7bV6e0ylMOKCz1YA; auth_ldaposso_authprovider=OSSO; _identity=a3debb1a5bf9a0b9935f3e03e30b3a553c47b1f0410e4b15f587bd046861a645a%3A2%3A%7Bi%3A0%3Bs%3A9%3A%22_identity%22%3Bi%3A1%3Bs%3A52%3A%22%5B%22150046%22%2C%222506c3066bac1e76681000a069cba8d6%22%2C604800%5D%22%3B%7D; _csrf=c05c3b15211877d2fde6f5fc5ef94ee7ab3c561c93a2cf57e46a8b2d8a12b9dfa%3A2%3A%7Bi%3A0%3Bs%3A5%3A%22_csrf%22%3Bi%3A1%3Bs%3A32%3A%22Uq6loTG-4dyV8s08c7YEKd8Rdfalq-Oo%22%3B%7D; _ym_d=1627278907; _ym_isad=2; PHPSESSID=v2a61p2qfka3gaugns0kcu2qfs