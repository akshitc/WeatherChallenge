package com.akshit.weatherchallenge.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.akshit.weatherchallenge.TestCoroutineRule
import com.akshit.weatherchallenge.data.CurrentWeatherModel
import com.akshit.weatherchallenge.data.WeatherModel
import com.akshit.weatherchallenge.data.WeatherRepository
import com.akshit.weatherchallenge.data.WeatherUIModel
import com.akshit.weatherchallenge.getOrAwaitValue
import com.akshit.weatherchallenge.utils.ResponseStatus
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var repository: WeatherRepository

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
    }

    @Test
    fun `Check success of weather API returns WeatherUIModel`() {
        testCoroutineRule.runBlockingTest {
            val viewModel = WeatherViewModel(repository)
            val stubWeatherModel = WeatherModel(
                CurrentWeatherModel(20.70, "Cloudy Day")
            )
            coEvery { repository.getWeather(59.337239, 18.062381) } returns ResponseStatus.Success(
                stubWeatherModel
            )
            viewModel.getWeather(59.337239, 18.062381)
            val value = viewModel.weatherUpdate.getOrAwaitValue()
            assertEquals(value, WeatherUIModel("21°F", "Cloudy Day"))
        }
    }

    @Test
    fun `Check failure of weather API shows an error`() {
        testCoroutineRule.runBlockingTest {
            val viewModel = WeatherViewModel(repository)
            coEvery { repository.getWeather(59.337239, 18.062381) } returns ResponseStatus.Error(
                IllegalStateException("API failed due to some error")
            )
            viewModel.getWeather(59.337239, 18.062381)
            val value = viewModel.error.getOrAwaitValue()
            assertEquals(value, true)
        }
    }
}
