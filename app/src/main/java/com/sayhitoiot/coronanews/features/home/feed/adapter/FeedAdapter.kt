package com.sayhitoiot.coronanews.features.home.feed.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.FeedAdapterPresent
import com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract.FeedAdapterPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract.FeedAdapterViewToPresenter
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val context: Context, private var statistics: MutableList<FeedEntity>):
    RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    companion object{
        const val TAG = "feed-adapter"
    }

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
                it.country.contains(charText)
            }
            statistics.addAll(clone)
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), FeedAdapterViewToPresenter{

        private val presenter: FeedAdapterPresenterToView by lazy {
            FeedAdapterPresent(this)
        }
        private var textCountry: TextView = itemView.itemFeed_textView_Country
        private var textNewCases: TextView = itemView.itemFeed_textView_newCases
        private var textRecoveries: TextView = itemView.itemFeed_textView_recovered
        private var textTotal: TextView = itemView.itemFeed_textView_Total
        private var textDeaths: TextView = itemView.itemFeed_textView_Deaths
        private var textUpdateTime: TextView = itemView.itemFeed_textView_date
        private var favoriteButton: ImageView = itemView.itemFeed_imageView_favorite
        private var viewTotal: View = itemView.itemFeed_view_total
        private var viewRecoveries: View = itemView.itemFeed_view_recovered
        private var viewDeath: View = itemView.itemFeed_view_deaths

        override var totalRecovered: Int? get() = textRecoveries.text.toString().toInt()
            set(value) {}
        override var totalConfirmed: Int? get() = textTotal.text.toString().toInt()
            set(value) {}
        override var totalDeaths: Int? get() = textDeaths.text.toString().toInt()
            set(value) {}
        override var country: String?
            get() = textCountry.text.toString()
            set(value) {}

        fun bind(feed: MutableList<FeedEntity>, position: Int){
            val dataFormat = feed[position].day.split("-")

            textCountry.text = feed[position].country
            textNewCases.text = feed[position].newCases
            textRecoveries.text = feed[position].recovereds.toString()
            textTotal.text = feed[position].cases.toString()
            textDeaths.text = feed[position].deaths.toString()
            textUpdateTime.text = "Atualizado em: ${dataFormat[2]}/${dataFormat[1]}/${dataFormat[0]}"
            favoriteButton.setOnClickListener { presenter.buttonFavoriteTapped() }
            presenter.requestUpdateGraph()

            if(feed[position].favorite){
                favoriteButton.imageTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorYellow))
            }

            Log.d("estatisticas", "r = $totalRecovered c= $totalConfirmed d=$totalDeaths")
            favoriteButton.backgroundTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorGreen))
        }

        override fun updateGraph(recoveries: Float, total: Float, deaths: Float) {

            viewRecoveries.layoutParams = LinearLayout.LayoutParams(
                viewRecoveries.layoutParams.width,
                viewRecoveries.layoutParams.height,
                recoveries
            )

            viewTotal.layoutParams = LinearLayout.LayoutParams(
                viewTotal.layoutParams.width,
                viewTotal.layoutParams.height,
                total
            )

            viewDeath.layoutParams = LinearLayout.LayoutParams(
                viewDeath.layoutParams.width,
                viewDeath.layoutParams.height,
                deaths
            )
        }

        override fun renderViewFavorite(color: Int) {
            Log.d(TAG, "color: $color")
            favoriteButton.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(context, color))
        }

        override fun updateAdapterWithFavorites(feedUpdated: MutableList<FeedEntity>) {
            statistics = feedUpdated
            notifyDataSetChanged()
        }
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}