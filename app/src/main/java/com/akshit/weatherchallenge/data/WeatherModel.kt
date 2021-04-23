package com.akshit.weatherchallenge.data

data class WeatherModel(val currently: CurrentWeatherModel)

data class CurrentWeatherModel(val temperature: Double, val summary: String)
