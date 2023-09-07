package space.septianrin.weatherappwithdi.module.homescreen.impl

import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService

class WeatherServiceImpl : WeatherService {
    override fun getRandomizedCity(): String {
        return arrayOf("Yogyakarta","Jakarta","Bali","Surakarta","Medan","Semarang").random()
    }
}