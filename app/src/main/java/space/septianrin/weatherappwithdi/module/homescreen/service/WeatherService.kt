package space.septianrin.weatherappwithdi.module.homescreen.service

import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData

interface WeatherService {
    fun getRandomizedCity(): String
}