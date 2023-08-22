package space.septianrin.weatherappwithdi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherService: WeatherService) : ViewModel() {
    // MutableLiveData to hold weather data
    private val _weatherLiveData = MutableLiveData<WeatherData>()

    // Expose an immutable LiveData for observing weather data
    val weatherLiveData: LiveData<WeatherData>
        get() = _weatherLiveData

    // Method to simulate fetching weather data
    fun fetchWeatherData() {
        val weatherDatas = weatherService.getListWeather()
        val weatherData = weatherDatas.random()
        Log.e("fetchWeatherData: ", "$weatherData")
        _weatherLiveData.postValue(weatherData)
    }
}