package space.septianrin.weatherappwithdi.module.homescreen.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
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
    private var isExpanded = false

    @Inject
    lateinit var apiKey: String

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        onListener()
        onObserve()

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

    private fun onObserve() {
        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            with(binding){
                cityNameTextView.text = weatherData.location.name
                currentTemperature.text = "Current : ${weatherData.current.tempCelsius}°C"
                feelLikeTemperature.text = "Feel Like : ${weatherData.current.feelslikeCelsius}°C"
                valueUV.text = "${weatherData.current.uv}"
                valueCloudPercent.text = "${weatherData.current.cloud}%"
                valueHumidityPercent.text = "${weatherData.current.humidity}"
                valueWindKph.text = "${weatherData.current.windKph}"
                valueWindDirection.text = "${weatherData.current.windDirection}"

                Glide.with(this@MainActivity)
                    .load(("http:" + weatherData.current.condition.icon).replace("64x64","128x128"))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(weatherImageView)
            }

        }
    }

    private fun onListener() {
        with(binding){
            randomizeWeather.setOnClickListener {
                val city = weatherViewModel.getRandomizedCity()
                weatherViewModel.getCityWeather(
                    city,
                    { response ->
                        weatherViewModel.saveData(response)
                    }, { error ->
                        Log.e("onCreate: ", error.toString())
                    })
            }

            weatherCardView.setOnClickListener {
                if (isExpanded) {
                    // Collapse the expandable layout
                    expandableLayout.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.collapse_animation));
                    expandableLayout.visibility = View.GONE
                    isExpanded = false
                } else {
                    // Expand the expandable layout
                    expandableLayout.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.expand_animation));
                    expandableLayout.visibility = View.VISIBLE
                    isExpanded = true
                }
            }
        }

    }
}