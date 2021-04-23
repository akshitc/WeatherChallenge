package com.akshit.weatherchallenge.data

import com.akshit.weatherchallenge.utils.ResponseStatus

interface WeatherRepository {

    suspend fun getWeather(latitude: Double, longitude: Double): ResponseStatus<WeatherModel>
}
