package com.akshit.weatherchallenge.utils

sealed class ResponseStatus<out T> {

    data class Success<out T>(val data: T) : ResponseStatus<T>()

    data class Error(val throwable: Throwable) : ResponseStatus<Nothing>()
}