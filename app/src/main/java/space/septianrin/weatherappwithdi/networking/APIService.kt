package space.septianrin.weatherappwithdi.networking

import WeatherResponse
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Query
import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData

interface APIService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey : String,
        @Query("q") location : String,
    ) : WeatherResponse

    @GET("forecast.json")
    fun getByReactive(
        @Query("key") apiKey : String,
        @Query("q") location : String,
        @Query("days") days : Int,
    ): Observable<WeatherResponse>

    @Headers("Content-Type: application/json")
    @HTTP(method = "get", path = "current.json", hasBody = true)
    fun getListByReactive(
        @Query("key") apiKey : String,
        @Query("q") query : String,
        @Body request : RequestBody,
    ): Single<List<WeatherResponse>>
}