package edu.kamshanski.tpuclassschedule.model.loader

import android.content.Context
import edu.kamshanski.tpuclassschedule.model.loader.converters.AnnotatedConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

/**
 * Retrofit client
 * Guides: https://habr.com/ru/post/520544/
 * @constructor Create empty Retrofit client
 */

@ExperimentalTime
@ExperimentalContracts
object RetrofitClient {
    @Volatile private var retrofit: Retrofit? = null
    val BASE_URL = "https://rasp.tpu.ru/"

    private fun client(context: Context): Retrofit {
        if (retrofit == null) {
            synchronized(this) {
                if (retrofit == null) {
                    retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(getOkHttpClient())
                        .addConverterFactory(AnnotatedConverterFactory.create(context))
                        .build()
                }
            }
        }

        return retrofit!!
    }

    private fun getOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .readTimeout(15000, TimeUnit.MILLISECONDS)
            .followRedirects(true)
            .callTimeout(15000, TimeUnit.MILLISECONDS)
            .addInterceptor(Interceptor { chain ->
                // Взято со StackOverflow
                var request: Request = chain.request()
                var response: Response = chain.proceed(request)
                if (response.code == 308) {
                    request = request.newBuilder()
                        .url(response.header("Location")!!)
                        .build()
                    response = chain.proceed(request)
                }
                response
            })
            .build()
    }

    fun getApi(context: Context): RaspApiService = client(context).create(RaspApiService::class.java)

}
