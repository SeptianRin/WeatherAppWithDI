package space.septianrin.weatherappwithdi.module.homescreen.impl

import space.septianrin.weatherappwithdi.module.homescreen.model.WeatherData
import space.septianrin.weatherappwithdi.module.homescreen.service.WeatherService

class WeatherServiceImpl : WeatherService {
    override fun getWeather(): WeatherData {
        // Simulate fetch data
        return WeatherData("Sunny", 25)
    }

    override fun getListWeather(): MutableList<WeatherData> {
        val weatherDatas = mutableListOf<WeatherData>()

        for (i in 1..20) {
            val condition = when (i % 4) {
                0 -> "Sunny"
                1 -> "Cloudy"
                2 -> "Rainy"
                else -> "Snowy"
            }

            val temperature = (i * 5) % 30 + 20 // Just a simple formula for temperature

            val weatherData = WeatherData(condition, temperature)
            weatherDatas.add(weatherData)
        }
        return weatherDatas
    }
}