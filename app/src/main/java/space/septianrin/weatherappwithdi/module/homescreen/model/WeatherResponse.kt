import com.google.gson.annotations.SerializedName
import space.septianrin.weatherappwithdi.module.homescreen.model.Current
import space.septianrin.weatherappwithdi.module.homescreen.model.Forecast
import space.septianrin.weatherappwithdi.module.homescreen.model.Location

data class WeatherResponse(
    @SerializedName("location")
    val location: Location,
    @SerializedName("current")
    val current: Current,
    @SerializedName("forecast")
    val forecast: Forecast
)