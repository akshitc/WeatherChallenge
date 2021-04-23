package com.akshit.weatherchallenge.utils

import retrofit2.Response

suspend fun <T : Any> safeApiCall(
    call: suspend () -> Response<T>
): ResponseStatus<T> =
    try {
        call.invoke().validateServerResponse()
    } catch (e: Exception) {
        ResponseStatus.Error(e)
    }

fun <T> Response<T>.validateServerResponse(): ResponseStatus<T> {
    val data = this.body()
    return if (this.isSuccessful && data != null) {
        ResponseStatus.Success(data)
    } else {
        ResponseStatus.Error(IllegalStateException("API failed to return data"))
    }
}
