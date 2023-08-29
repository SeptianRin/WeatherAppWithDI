package space.septianrin.weatherappwithdi.module.homescreen.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Scheduler
import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData
import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService
import space.septianrin.weatherappwithdi.networking.APIService
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherService: WeatherService,
    private val apiService: APIService,
    @Named("schedulerIo") private val schedulersIo: Scheduler,
    @Named("mainThread") private val mainThread: Scheduler?,
    private val apiKey : String
) :
    ViewModel() {
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

    @SuppressLint("CheckResult")
    fun fetchByRxJava() {
        apiService.getListWeather(
            apiKey = apiKey,
            location = "dubai"
        )
            .subscribeOn(schedulersIo)
            .observeOn(mainThread)
            .subscribe(
                { onSuccess ->
                    Log.e("fetchByRxJava: ", "SUCCESS : $onSuccess")

                },
                { onError ->
                    Log.e("fetchByRxJava: ", "ERROR : $onError")
                }
            )
    }

    fun bulkRequest(){
        "{\n" +
                "    \"locations\": [\n" +
                "        {\n" +
                "            \"q\": \"53,-0.12\",\n" +
                "            \"custom_id\": \"my-id-1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"q\": \"London\",\n" +
                "            \"custom_id\": \"any-internal-id\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"q\": \"90201\",\n" +
                "            \"custom_id\": \"us-zipcode-id-765\"\n" +
                "        }\n" +
                "    ]\n" +
                "}\n" +
                "    "
    }
}