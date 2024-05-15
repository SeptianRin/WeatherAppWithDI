package space.septianrin.weatherappwithdi.utils

import android.app.Activity
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    private val GLOBAL_DATETIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    fun vibratePhone(activity: Activity) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                activity.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            activity.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun String.getHourFormat(): String {
        val hourlyFormatter = DateTimeFormatter.ofPattern("HH:mm")
        return LocalDateTime.parse(this, GLOBAL_DATETIME_FORMAT).format(hourlyFormatter)
    }

    fun String.getHour(): Int {
        val dateTime = LocalDateTime.parse(this, GLOBAL_DATETIME_FORMAT)
        return dateTime.hour
    }

    fun String.getHourWithAMPMFormat(): String {
        val formatAMPM = DateTimeFormatter.ofPattern("h a")
        return LocalDateTime.parse(this, GLOBAL_DATETIME_FORMAT).format(formatAMPM)
    }

    fun String.getThreeLetterDay(): String {
        val date = LocalDateTime.parse(this, GLOBAL_DATETIME_FORMAT)
        return date.format(DateTimeFormatter.ofPattern("EEE"))
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }

    enum class IconHelper(val code: Int) {
        Sunny(1000),
        PartlyCloudly(1003),
        Cloudy(1006),
        Overcast(1009),
        Mist(1030),
    }
}