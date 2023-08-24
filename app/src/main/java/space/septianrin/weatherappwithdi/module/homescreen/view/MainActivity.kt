package space.septianrin.weatherappwithdi.module.homescreen.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import space.septianrin.weatherappwithdi.BuildConfig
import space.septianrin.weatherappwithdi.module.homescreen.viewmodel.WeatherViewModel
import space.septianrin.weatherappwithdi.databinding.ActivityMainBinding
import space.septianrin.weatherappwithdi.viewmodel.NetworkingViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val networkViewModel: NetworkingViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var apiKey : String

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
        lifecycleScope.launch {
            val weatherData = networkViewModel.fetchWeather("London", apiKey)
            Log.e("onCreate: ", "$weatherData")
        }
    }
}