package space.septianrin.weatherappwithdi.module.homescreen.model

import com.google.gson.annotations.SerializedName

data class Forecast(
    @SerializedName("forecastday")
    val forecastday: List<ForecastDay>
)