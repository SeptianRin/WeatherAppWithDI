import com.google.gson.annotations.SerializedName

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id") val tzId: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    val localtime: String
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

data class CurrentWeather(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val tempCelsius: Double,
    @SerializedName("temp_f") val tempFahrenheit: Double,
    @SerializedName("is_day") val isDay: Int,
    val condition: Condition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDirection: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureIn: Double,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c") val feelslikeCelsius: Double,
    @SerializedName("feelslike_f") val feelslikeFahrenheit: Double,
    @SerializedName("vis_km") val visibilityKm: Double,
    @SerializedName("vis_miles") val visibilityMiles: Double,
    val uv: Double,
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double
)

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather
)