package space.septianrin.weatherappwithdi.module.homescreen.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
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
    private val REQUEST_LOCATION_PERMISSION = 123

    //TODO:Add Location permission and fill the data with current location

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initView()
        onListener()
        onObserve()
        requestLocation()
        initState()


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

    private fun initState() {
        weatherViewModel.saveUnit("C")
        binding.switchTempUnit.isChecked = false
    }

    private fun initView() {
        with(binding) {
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
        val tempUnit = weatherViewModel.tempUnit
        val weather = weatherViewModel.weatherLiveData
        tempUnit.observe(this) { tempUnit ->
            weather.observe(this) { weatherData ->
                //Big Card
                with(binding) {
                    cityNameTextView.text = weatherData.location.name
                    currentTemperature.text = if (tempUnit == "C") {
                        "${weatherData.current.tempC}°C"
                    } else {
                        "${weatherData.current.tempF}°F"
                    }
                    feelLikeTemperature.text = if (tempUnit == "C") {
                        "Feel Like : ${weatherData.current.feelslikeC}°C"
                    } else {
                        "Feel Like : ${weatherData.current.feelslikeF}°F"
                    }
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
                    hourlyAdapter.updateTempUnit(tempUnit)
                    val rightNow = Calendar.getInstance()
                    val currentHour: Int = rightNow.get(Calendar.HOUR_OF_DAY)
                    rvHourlyForecast.scrollToPosition(currentHour)
                    dailyAdapter.updateData(weatherData.forecast.forecastday)
                    dailyAdapter.updateTempUnit(tempUnit)
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
                    }
                )
            }

            weatherCardView.setOnClickListener { expandCard() }
            btnExpand.setOnClickListener { expandCard() }
            btnAddLocation.setOnClickListener {
                getCurrentLocationWeather()
            }
            switchTempUnit.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    weatherViewModel.saveUnit("F")
                } else {
                    weatherViewModel.saveUnit("C")
                }
            }
            btnSeeMore.setOnClickListener {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(urlShare)
                startActivity(i)
            }
        }
    }

    private fun requestLocation() {
        val permission = Manifest.permission.ACCESS_COARSE_LOCATION
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            // Permission already granted, proceed with location retrieval
            getLocation()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather()
            } else {
                Toast.makeText(
                    this,
                    "Location Access Denied, Feature won't work properly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getCurrentLocationWeather() {
        // Initialize the FusedLocationProviderClient
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

// Define a LocationRequest
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(5000)
            .setMaxUpdateDelayMillis(10000)
            .build()

// Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    val latitude = location.latitude
                    val longitude = location.longitude

                    weatherViewModel.getCityWeather("$latitude,$longitude", { weatherResponse ->
                        weatherViewModel.saveData(weatherResponse)
                    }, {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to get current Location",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    })


                    // Use latitude and longitude for weather report or any other purpose
                }
            }
        }, null)

    }

    private fun expandCard() {
        with(binding) {
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
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.round_keyboard_arrow_down_24
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
                arrowPost.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.round_keyboard_arrow_up_24
                    )
                )
                isExpanded = true
            }
        }
    }
}