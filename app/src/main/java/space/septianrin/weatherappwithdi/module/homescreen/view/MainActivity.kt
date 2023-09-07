package space.septianrin.weatherappwithdi.module.homescreen.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    private var urlShare = "https://www.weatherapi.com/weather/q/"

    //Hourly Forecast
    private val hourlyAdapter = HourlyForecastAdapter(this)
    //Daily Forecast
    private val dailyAdapter = DailyForecastAdapter(this)

    //TODO:Add Location permission and fill the data with current location

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        onListener()
        onObserve()

        lifecycleScope.launch {
            val city = weatherViewModel.getRandomizedCity()
            weatherViewModel.getCityWeather(
                city,
                { response ->
                    weatherViewModel.saveData(response)
                }, { error ->
                    Log.e("onCreate: ", error.toString())
                })
        }
    }

    private fun initView() {
        with(binding){
            randomizeWeather.show()
            weatherCardView.show()
            btnSeeMore.show()
            toolbarIcons.show()
            rvHourlyForecast.adapter = hourlyAdapter
            rvDailyForecast.apply {
                adapter = dailyAdapter
                isNestedScrollingEnabled = false
            }
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
                urlShare = "https://www.weatherapi.com/weather/q/${weatherData.location.name}"
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

                hourlyAdapter.updateData(weatherData.forecast.forecastday[0].hour)
                val rightNow = Calendar.getInstance()
                val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
                rvHourlyForecast.scrollToPosition(currentHour)
                dailyAdapter.updateData(weatherData.forecast.forecastday)
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
                    }
                )
            }

            weatherCardView.setOnClickListener { expandCard() }
            btnExpand.setOnClickListener { expandCard() }
            btnAddLocation.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Add Location",
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnSetting.setOnClickListener {
                Toast.makeText(
                    applicationContext,
                    "Add Setting",
                    Toast.LENGTH_SHORT
                ).show()
            }
            btnSeeMore.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(urlShare)
                startActivity(i)
            }
        }
    }

    private fun expandCard() {
        with(binding){
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
                arrowPost.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,
                        R.drawable.round_keyboard_arrow_down_24
                    ))
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
                arrowPost.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext,
                        R.drawable.round_keyboard_arrow_up_24
                    ))
                isExpanded = true
            }
        }
    }
}