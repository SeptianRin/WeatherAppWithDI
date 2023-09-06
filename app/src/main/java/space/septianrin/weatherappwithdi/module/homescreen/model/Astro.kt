package space.septianrin.weatherappwithdi.module.homescreen.model

import com.google.gson.annotations.SerializedName

data class Astro(
    @SerializedName("sunrise")
    val sunrise: String,
    @SerializedName("sunset")
    val sunset: String,
    @SerializedName("moonrise")
    val moonrise: String,
    @SerializedName("moonset")
    val moonset: String
)