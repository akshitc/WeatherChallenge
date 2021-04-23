package com.akshit.weatherchallenge.network

import com.akshit.weatherchallenge.data.WeatherModel
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val API_BASE_URL = "https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/"

interface WeatherService {

    @GET("{latitude},{longitude}")
    suspend fun getWeather(
        @Path("latitude") latitude: Double,
        @Path("longitude") longitude: Double
    ): Response<WeatherModel>

    companion object {

        fun createService(): WeatherService {
            val client = OkHttpClient.Builder().build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_BASE_URL)
                .client(client)
                .build()
            return retrofit.create(WeatherService::class.java)
        }
    }
}
