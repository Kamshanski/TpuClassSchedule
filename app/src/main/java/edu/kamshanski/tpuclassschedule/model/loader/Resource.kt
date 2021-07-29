package edu.kamshanski.tpuclassschedule.model.loader
//https://blog.mindorks.com/using-retrofit-with-kotlin-coroutines-in-android

class Resource<out T>(val data: T?, val status: LoadStatus, val message: String?, val error: Throwable? = null) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(data,  LoadStatus.SUCCESS, null)

        fun <T> error(error: Throwable, message: String? = null, data: T? = null,): Resource<T> =
            Resource(data, LoadStatus.ERROR, message, error)

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(data, LoadStatus.LOADING, null)

        fun <T> new(message: String? = null): Resource<T> =
            Resource(data = null, LoadStatus.CREATED, message, null)
    }
}