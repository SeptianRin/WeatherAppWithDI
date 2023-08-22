package space.septianrin.weatherappwithdi.module.homescreen.service

import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData

interface WeatherService {
    fun getWeather(): WeatherData
    fun getListWeather(): MutableList<WeatherData>
}