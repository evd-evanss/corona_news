package com.sayhitoiot.coronanews.features.home.feed

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.api.model.Response
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val statistics: MutableList<FeedEntity>):
    RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    private var cloneStatistics = statistics


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_feed))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(statistics, position)
    }

    override fun getItemCount() = statistics.size

    fun updateList(properties: MutableList<FeedEntity>){
        this.statistics.clear()
        this.statistics.addAll(properties)
        cloneStatistics = properties
        notifyDataSetChanged()
    }

    fun filter(charText: String) {
        statistics.clear()
        if (charText.isEmpty()) {
            statistics.addAll(cloneStatistics)
        } else {
            val clone = cloneStatistics.filter {
                it.country == charText
            }
            statistics.addAll(clone)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){


        fun bind(result: MutableList<FeedEntity>, position: Int){
            val dataFormat = result[position].day.split("-")

            if(position == 0) {
                itemView.statistic_textView_title.visibility = VISIBLE
            } else {
                itemView.statistic_textView_title.visibility = GONE
            }

            itemView.statistic_textView_Country.text = result[position].country
            itemView.statistic_textView_newCases.text = result[position].newCases
            itemView.statistic_textView_recovered.text = result[position].recovereds.toString()
            itemView.statistic_textView_Total.text = result[position].cases.toString()
            itemView.statistic_textView_Deaths.text = result[position].deaths.toString()
            itemView.statistic_textView_date.text = "${dataFormat[2]}/${dataFormat[1]}/${dataFormat[0]}"
            val totalRecovered = result[position].recovereds.toFloat() / 10000
            val totalConfirmed = result[position].cases.toFloat() / 10000
            val totalDeaths = result[position].deaths.toFloat() / 10000

            Log.d("estatisticas", "r = $totalRecovered c= $totalConfirmed d=$totalDeaths")

            updateStatisticsGraph(
                itemView.statistic_view_confirmed,
                itemView.statistic_view_recovered,
                itemView.statistic_view_deaths,
                totalConfirmed,
                totalRecovered,
                totalDeaths)
        }

        private fun updateStatisticsGraph(
            total: View,
            recoveries: View,
            deaths: View,
            confirmedWeigh: Float,
            recoveredWeight: Float,
            deathsWeight: Float
        ) {

            total.layoutParams = LinearLayout.LayoutParams(
                total.layoutParams.width,
                total.layoutParams.height,
                confirmedWeigh
            )

            recoveries.layoutParams = LinearLayout.LayoutParams(
                recoveries.layoutParams.width,
                recoveries.layoutParams.height,
                recoveredWeight
            )

            deaths.layoutParams = LinearLayout.LayoutParams(
                deaths.layoutParams.width,
                deaths.layoutParams.height,
                deathsWeight
            )
        }
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}