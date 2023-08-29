package space.septianrin.weatherappwithdi.networking

import WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData

interface APIService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey : String,
        @Query("q") location : String,
    ) : WeatherResponse

    @GET("forecast.json")
    fun getListWeather(
        @Query("key") apiKey : String,
        @Query("q") location : String,
    ): Single<List<WeatherResponse>>
}