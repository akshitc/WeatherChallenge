package com.akshit.weatherchallenge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.akshit.weatherchallenge.data.WeatherRepository
import com.akshit.weatherchallenge.data.WeatherRepositoryImpl
import com.akshit.weatherchallenge.network.WeatherService
import com.akshit.weatherchallenge.viewModels.WeatherViewModel

object Injection {

    private fun provideCatRepository(): WeatherRepository {
        return WeatherRepositoryImpl(WeatherService.createService())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return WeatherViewModel(provideCatRepository()) as T
            }
        }
    }
}
