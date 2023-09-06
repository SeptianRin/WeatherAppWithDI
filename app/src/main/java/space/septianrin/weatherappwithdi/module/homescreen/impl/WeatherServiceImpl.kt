package space.septianrin.weatherappwithdi.module.homescreen.impl

import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService

class WeatherServiceImpl : WeatherService {
    override fun getRandomizedCity(): String {
        return arrayOf("Jakarta","Bali","Solo","Medan","Semarang").random()
    }

}