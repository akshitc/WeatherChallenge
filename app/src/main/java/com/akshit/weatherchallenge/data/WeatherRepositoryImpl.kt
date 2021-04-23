package com.akshit.weatherchallenge.data

import com.akshit.weatherchallenge.network.WeatherService
import com.akshit.weatherchallenge.utils.ResponseStatus
import com.akshit.weatherchallenge.utils.safeApiCall

class WeatherRepositoryImpl(private val weatherService: WeatherService) : WeatherRepository {

    override suspend fun getWeather(
        latitude: Double,
        longitude: Double
    ): ResponseStatus<WeatherModel> {
        return safeApiCall { weatherService.getWeather(latitude, longitude) }
    }
}
