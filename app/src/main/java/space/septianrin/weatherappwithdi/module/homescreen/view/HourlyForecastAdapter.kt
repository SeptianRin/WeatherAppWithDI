package space.septianrin.weatherappwithdi.module.homescreen.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import space.septianrin.weatherappwithdi.R
import space.septianrin.weatherappwithdi.databinding.ItemHourlyForecastBinding
import space.septianrin.weatherappwithdi.module.homescreen.model.Hour
import space.septianrin.weatherappwithdi.utils.Utils
import space.septianrin.weatherappwithdi.utils.Utils.getHourFormat
import space.septianrin.weatherappwithdi.utils.Utils.getHourWithAMPMFormat
import space.septianrin.weatherappwithdi.utils.Utils.gone
import space.septianrin.weatherappwithdi.utils.Utils.show

class HourlyForecastAdapter(private val context: Context) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

    private var hourlyItemList: List<Hour> = listOf()
    private var tempUnit: String = "C"

    fun updateData(newData : List<Hour>) {
        hourlyItemList = newData
        notifyDataSetChanged()
    }
    fun updateTempUnit(tempUnit: String) {
        this.tempUnit = tempUnit
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HourlyForecastAdapter.ViewHolder {
        val binding = ItemHourlyForecastBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyForecastAdapter.ViewHolder, position: Int) {
        val item = hourlyItemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return hourlyItemList.size
    }

    inner class ViewHolder(private val binding: ItemHourlyForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Hour) {
            with(binding) {
                tvHourly.text = item.time.getHourWithAMPMFormat()
                when(item.condition.code){
                    Utils.IconHelper.Sunny.code -> {
                        ivHourly.gone()
                        avHourly.show()
                    }

                    else -> {
                        Glide.with(context)
                            .load("http:" + item.condition.icon)
                            .placeholder(null)
                            .error(R.drawable.ic_launcher_background)
                            .into(ivHourly)
                        ivHourly.show()
                        avHourly.gone()
                    }
                }
                Glide.with(context)
                    .load("http:" + item.condition.icon)
                    .placeholder(null)
                    .error(R.drawable.ic_launcher_background)
                    .into(ivHourly)
                tvTemp.text = if(tempUnit == "C"){"${item.tempC}°C"} else{"${item.tempF}°F"}
            }
        }
    }
}