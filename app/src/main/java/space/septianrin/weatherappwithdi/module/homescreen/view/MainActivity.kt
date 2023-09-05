package space.septianrin.weatherappwithdi.module.homescreen.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import space.septianrin.weatherappwithdi.R
import space.septianrin.weatherappwithdi.databinding.ActivityMainBinding
import space.septianrin.weatherappwithdi.module.homescreen.viewmodel.WeatherViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var apiKey: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.randomizeWeather.setOnClickListener {
            val city = weatherViewModel.getRandomizedCity()
            weatherViewModel.getCityWeather(
                city,
                { response ->
                    weatherViewModel.saveData(response)
                }, { error ->
                    Log.e("onCreate: ", error.toString())
                })
        }
        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            binding.weatherTextView.text =
                "${weatherData.location.name} : ${weatherData.current.condition.text} => ${weatherData.current.tempCelsius}°C"
            Glide.with(this)
                .load(("http:" + weatherData.current.condition.icon).replace("64x64","128x128"))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.weatherImageView)
        }
        lifecycleScope.launch {
            weatherViewModel.getCityWeather(
                "London",
                { response ->
                    weatherViewModel.saveData(response)
                }, { error ->
                    Log.e("onCreate: ", error.toString())
                })
        }
    }
}