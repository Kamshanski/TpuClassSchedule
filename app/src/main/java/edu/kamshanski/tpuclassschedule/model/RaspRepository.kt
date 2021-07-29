package edu.kamshanski.tpuclassschedule.model

import android.app.Application
import edu.kamshanski.tpuclassschedule.model.loader.RaspLoadHelper
import edu.kamshanski.tpuclassschedule.model.loader.RetrofitClient
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.YearTimeRange
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
class RaspRepository(application: Application) {
    val raspLoader by lazy { RaspLoadHelper(RetrofitClient.getApi(application)) }

    suspend fun getSources(searchInput: String) = raspLoader.getSources(searchInput)

    suspend fun getSourceInfo(source: ShortSource) = raspLoader.getSourceInfo(source)

    suspend fun getYearTimeRange(year: Int) : YearTimeRange {
        // Тут круто будет всунуть получение YearReferences сначала из настроек, а в случае неналичия из сети
        return raspLoader.getYearTimeRange(year)
    }

    suspend fun getCurrentYear() : Int {
        // Тут круто будет всунуть получение YearReferences сначала из настроек, а в случае неналичия из сети
        return raspLoader.getCurrentYear()
    }



}