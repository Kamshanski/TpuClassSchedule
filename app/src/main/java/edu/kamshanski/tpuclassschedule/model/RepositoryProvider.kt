package edu.kamshanski.tpuclassschedule.model

import android.app.Application
import java.lang.ref.WeakReference
import kotlin.contracts.ExperimentalContracts
import kotlin.time.ExperimentalTime

@ExperimentalContracts
@ExperimentalTime
object RepositoryProvider {
    @Volatile var weakRepository = WeakReference<RaspRepository>(null)
    fun rasp(application: Application) : RaspRepository {
        if (weakRepository.get() == null) {
            synchronized(RaspRepository::class) {
                if (weakRepository.get() == null) {
                    weakRepository = WeakReference(RaspRepository(application))
                }
            }
        }
        return weakRepository.get()!!
    }
}