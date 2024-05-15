package space.septianrin.weatherappwithdi.module.homescreen.viewmodel

import WeatherResponse
import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import space.septianrin.weatherappwithdi.model.RxSchedulers
import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService
import space.septianrin.weatherappwithdi.networking.APIService
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherService: WeatherService,
    private val apiService: APIService,
    private val schedulers: RxSchedulers,
    private val apiKey: String
) :
    ViewModel() {

    private val FORECAST_DAY = 5

    // MutableLiveData to hold weather data
    private val _weatherLiveData = MutableLiveData<WeatherResponse>()
    private val _tempUnit = MutableLiveData<String>()

    // Expose an immutable LiveData for observing weather data
    val weatherLiveData: LiveData<WeatherResponse>
        get() = _weatherLiveData

    val tempUnit: LiveData<String>
        get() = _tempUnit


    // Method to simulate fetching weather data
    fun saveData(weatherData: WeatherResponse) {
        _weatherLiveData.postValue(weatherData)
    }

    fun saveUnit(unit: String){
        _tempUnit.postValue(unit)
    }

    fun getRandomizedCity(): String {
        return weatherService.getRandomizedCity()
    }

    @SuppressLint("CheckResult")
    fun getCityWeather(
        city: String,
        onSuccess: (WeatherResponse) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        apiService.getByReactive(apiKey, city, FORECAST_DAY)
            .subscribeOn(schedulers.io)
            .observeOn(schedulers.main)
            .subscribe(onSuccess, onError)
    }

    suspend fun fetchCityWeather(city: String): WeatherResponse = withContext(Dispatchers.IO){
        return@withContext apiService.getCurrentWeatherByCoroutine(apiKey, city,FORECAST_DAY)
    }
}