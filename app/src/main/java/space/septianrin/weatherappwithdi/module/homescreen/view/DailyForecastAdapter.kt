package space.septianrin.weatherappwithdi.module.homescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import space.septianrin.weatherappwithdi.R
import space.septianrin.weatherappwithdi.databinding.ItemDailyForecastBinding
import space.septianrin.weatherappwithdi.module.homescreen.model.ForecastDay
import space.septianrin.weatherappwithdi.utils.Utils
import space.septianrin.weatherappwithdi.utils.Utils.getHourFormat
import space.septianrin.weatherappwithdi.utils.Utils.getThreeLetterDay
import space.septianrin.weatherappwithdi.utils.Utils.gone
import space.septianrin.weatherappwithdi.utils.Utils.show

class DailyForecastAdapter(
    private val context: Context,
) : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {

    private var dailyItemList: List<ForecastDay> = listOf()
    private var tempUnit: String = "C"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDailyForecastBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    fun updateData(newData: List<ForecastDay>) {
        dailyItemList = newData
        notifyDataSetChanged()
    }

    fun updateTempUnit(tempUnit: String) {
        this.tempUnit = tempUnit
    }

    override fun getItemCount(): Int {
        return dailyItemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dailyItemList[position]
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemDailyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ForecastDay) {
            with(binding) {
                tvHour.text = item.hour[(0..23).random()].time.getHourFormat()
                tvDay.text = item.hour[(0..23).random()].time.getThreeLetterDay()
                when(item.hour[8].condition.code){
                    Utils.IconHelper.Sunny.code -> {
                        ivHourly.gone()
                        avHourly.show()
                    }

                    else -> {
                        Glide.with(context)
                            .load("http:" + item.hour[8].condition.icon)
                            .placeholder(null)
                            .error(R.drawable.ic_launcher_background)
                            .into(ivHourly)
                        ivHourly.show()
                        avHourly.gone()
                    }
                }

                tvTemp.text = if (tempUnit == "C") {
                    "${item.day.mintempC} / ${item.day.maxtempC}°C"
                } else {
                    "${item.day.mintempF} / ${item.day.maxtempF}°F"
                }
            }
        }
    }
}