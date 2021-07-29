package edu.kamshanski.tpuclassschedule.activities.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.kamshanski.tpuclassschedule.activities._abstract.BaseViewModel
import edu.kamshanski.tpuclassschedule.model.RepositoryProvider
import edu.kamshanski.tpuclassschedule.model.loader.Resource
import edu.kamshanski.tpuclassschedule.model.preferences.CommonPreferences
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.ShortSource
import edu.kamshanski.tpuclassschedule.model.rasp_entities.schedule_meta.Source
import edu.kamshanski.tpuclassschedule.utils.*
import edu.kamshanski.tpuclassschedule.utils.collections.insert
import edu.kamshanski.tpuclassschedule.utils.nice_classes.lgTry
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlin.Exception
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
class MainViewModel(app: Application) : BaseViewModel(app) {
    private val raspRepository = RepositoryProvider.rasp(app)
    private val sourceList = MutableLiveData<Resource<List<ShortSource>>>(Resource.new())
    private val _fullSourcesMap = HashMap<String, Source>()
    val fullSources = MutableLiveData<Map<String, Source>>(_fullSourcesMap)
    val loadedSources = MutableSharedFlow<Source>(replay = 1, extraBufferCapacity = 3)

    init {
        inMain {
            loadedSources.collect {
                _fullSourcesMap[it.hash] = it
                fullSources.notify()
            }
        }
    }

    fun getSources(searchInput: String) : LiveData<Resource<List<ShortSource>>> {
        inIO {
            sourceList.postValue(Resource.loading(emptyList()))
            try {
                val list = raspRepository.getSources(searchInput)
                sourceList.postValue(Resource.success(list))

            } catch (ex: Exception) {
                sourceList.postValue(Resource.error(ex, "Error loading sources"))
            }
        }
        return sourceList
    }

    // TODO: может лучше заменить на inDefault вместо inIO
    // не будет использлваться для отображения на экране поиска. Только в бэкграунде при загрузке
    fun getSourceInfo(source: ShortSource) {
        startTimer("source")
        inDefault {
            lgTry({"$source info loading failed with an error:"}) {
                val fullSource = raspRepository.getSourceInfo(source)
                loadedSources.emit(fullSource)

                sourceList.notifyAsync()

                val year = raspRepository.getCurrentYear()      // just to check now

                lg("getCurrentYear: $year")
                val range = raspRepository.getYearTimeRange(year)
                lg("Time range of ${range.year} year: ${range.firstDate} - ${range.lastDate} (${range.weeks} week)")

                // save found data
                val prefs = CommonPreferences(getApplication())
                prefs.yearsTimeRanges = prefs.yearsTimeRanges.insert(range)
                prefs.selectedSources = prefs.selectedSources.insert(source)

                stopTimerAndPrint("source")
            }
        }
    }

    // не будет использлваться для отображения на экране поиска. Только в бэкграунде при загрузке
    fun getCurrentYear() {
        inDefault {
            lgTry({"Current year loading failed with an error:"}) {
                val year = raspRepository.getCurrentYear()
            }
        }
    }

    // не будет использлваться для отображения на экране поиска. Только в бэкграунде при загрузке
    fun getYearTimeRange(year: Int) {
        inDefault {
            lgTry({"$year time range loading failed with an error:"}) {
                val references = raspRepository.getYearTimeRange(year)
            }
        }
    }
}