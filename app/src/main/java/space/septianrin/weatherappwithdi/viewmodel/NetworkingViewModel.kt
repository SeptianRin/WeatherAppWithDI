package space.septianrin.weatherappwithdi.viewmodel

import WeatherResponse
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import space.septianrin.weatherappwithdi.networking.APIService
import javax.inject.Inject

@HiltViewModel
class NetworkingViewModel @Inject constructor(private val weatherApiService: APIService) : ViewModel() {
    suspend fun fetchWeather(location: String, apiKey: String) : WeatherResponse{
        return weatherApiService.getCurrentWeatherByCoroutine(apiKey,location,7)
    }

}