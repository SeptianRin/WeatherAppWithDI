package space.septianrin.weatherappwithdi.module.homescreen.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
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
import space.septianrin.weatherappwithdi.utils.Utils
import space.septianrin.weatherappwithdi.utils.Utils.gone
import space.septianrin.weatherappwithdi.utils.Utils.show
import java.util.Calendar
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

    @SuppressLint("SetTextI18n")
    private fun onObserve() {
        weatherViewModel.weatherLiveData.observe(this) { weatherData ->
            //Big Card
            with(binding) {
                cityNameTextView.text = weatherData.location.name
                currentTemperature.text = "${weatherData.current.tempC}°C"
                feelLikeTemperature.text = "Feel Like : ${weatherData.current.feelslikeC}°C"
                randomizeWeather.show()
                weatherCardView.show()
                valueUV.text = "${weatherData.current.uv}"
                valueCloudPercent.text = "${weatherData.current.cloud}%"
                valueHumidityPercent.text = "${weatherData.current.humidity}"
                valueWindKph.text = "${weatherData.current.windKph}"
                valueWindDirection.text = weatherData.current.windDir

                Glide.with(this@MainActivity)
                    .load("http:" + weatherData.current.condition.icon)
                    .placeholder(null)
                    .error(R.drawable.ic_launcher_background)
                    .into(weatherImageView)

                //Hourly Forecast
                val hourlyAdapter = HourlyForecastAdapter(
                    this@MainActivity.applicationContext,
                    weatherData.forecast.forecastday[0].hour
                )
                val rightNow = Calendar.getInstance()
                val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
                rvHourlyForecast.apply {
                    adapter = hourlyAdapter
                    scrollToPosition(currentHour)
                }

                //Daily Forecast
                val dailyAdapter = DailyForecastAdapter(
                    this@MainActivity.applicationContext,
                    weatherData.forecast.forecastday
                )
                rvDailyForecast.apply {
                    adapter = dailyAdapter
                    isNestedScrollingEnabled = false
                }
            }


        }
    }

    private fun onListener() {
        with(binding) {
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
                Utils.vibratePhone(this@MainActivity)
                TransitionManager.beginDelayedTransition(this.root)
                if (isExpanded) {
                    // Collapse the expandable layout
                    expandableLayout.startAnimation(
                        AnimationUtils.loadAnimation(
                            applicationContext,
                            R.anim.collapse_animation
                        )
                    )
                    expandableLayout.gone()
                    isExpanded = false
                } else {
                    // Expand the expandable layout
                    expandableLayout.startAnimation(
                        AnimationUtils.loadAnimation(
                            applicationContext,
                            R.anim.expand_animation
                        )
                    )
                    expandableLayout.show()
                    isExpanded = true
                }
            }
        }

    }
}