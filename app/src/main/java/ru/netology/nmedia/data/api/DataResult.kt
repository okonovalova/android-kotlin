package ru.netology.nmedia.data.api

data class DataResult<out T>(val status: Status, val data: T?, val error: AppError?) {

    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> success(data: T?): DataResult<T> {
            return DataResult(Status.SUCCESS, data, null)
        }

        fun <T> error(error: AppError?): DataResult<T> {
            return DataResult(Status.ERROR, null, error)
        }
    }

    override fun toString(): String {
        return "Result(status=$status, data=$data, error=$error)"
    }
}