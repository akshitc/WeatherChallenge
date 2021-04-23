package com.akshit.weatherchallenge.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akshit.weatherchallenge.data.WeatherRepository
import com.akshit.weatherchallenge.data.WeatherUIModel
import com.akshit.weatherchallenge.utils.ResponseStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    private val _weatherUpdate = MutableLiveData<WeatherUIModel>()
    val weatherUpdate: LiveData<WeatherUIModel> = _weatherUpdate

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            val weatherModel = weatherRepository.getWeather(latitude, longitude)
            _loading.postValue(false)
            when (weatherModel) {
                is ResponseStatus.Success -> {
                    val temp =
                        weatherModel.data.currently.temperature.roundToInt().toString() + "°F"
                    val uiModel = WeatherUIModel(
                        temperature = temp,
                        description = weatherModel.data.currently.summary
                    )
                    _weatherUpdate.postValue(uiModel)
                }
                is ResponseStatus.Error -> {
                    _error.postValue(true)
                }
            }
        }
    }
}
