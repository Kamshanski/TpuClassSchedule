package edu.kamshanski.tpuclassschedule.model.loader

import androidx.annotation.Size
import edu.kamshanski.tpuclassschedule.model.loader.converters.*
import edu.kamshanski.tpuclassschedule.model.loader.responses.WeekScheduleResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourceResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.SourcesResponse
import edu.kamshanski.tpuclassschedule.model.loader.responses.TimeReferenceResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

/**
 * https://blog.mindorks.com/using-retrofit-with-kotlin-coroutines-in-android
 *
 */

@ExperimentalContracts
@ExperimentalTime
interface RaspApiService {
    // Загружать надо сразу все, потому что необходимо здесь же отсеивать непригодные данные
    @Converter(SourcesListConverter::class)
    @GET("select/search/main.html?page=1&page_limit=5000")
    suspend fun getSources(@Query("q") searchInput: String) : SourcesResponse

    @Converter(SourceConverter::class)
    @GET("redirect/kalendar.html")
    suspend fun getSourceInfo(@Query("hash") hash: String) : SourceResponse

    @Converter(CurrentYearConverter::class)
    @GET(".")
    suspend fun getCurrentYear() : Int

    // Values inside @IntRange was obtained experimentally. 2099 is an deadline for my app developing )))
    @Converter(TimeReferenceConverter::class)
    @GET("{link}/{year}/{week}/view.html")
    suspend fun getYearTimeRange(
        @Path("link") link: String,
        @Path("year") @Size(4) year: String,
        @Path("week") @Size(2) week: String) : TimeReferenceResponse

    // Values inside @IntRange was obtained experimentally. 2099 is an deadline for my app developing )))
    @Converter(WeekScheduleConverter::class)
    @GET("{link}/{year}/{week}/view.html")
    suspend fun getSchedule(@Path("link") link: String,
                            @Path("year") @Size(4) year: String,
                            @Path("week") @Size(2) week: String) : WeekScheduleResponse
}