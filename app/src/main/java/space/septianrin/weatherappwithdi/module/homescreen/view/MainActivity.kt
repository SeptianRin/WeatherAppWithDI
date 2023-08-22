package space.septianrin.weatherappwithdi.module.homescreen.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import space.septianrin.weatherappwithdi.module.homescreen.viewmodel.WeatherViewModel
import space.septianrin.weatherappwithdi.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.randomizeWeather.setOnClickListener {
            weatherViewModel.fetchWeatherData()
        }
        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            val weatherText =
                "Condition: ${weatherData.condition}, Temperature: ${weatherData.temperature}°C"
            binding.weatherTextView.text = weatherText
        }
        weatherViewModel.fetchWeatherData()
    }
}