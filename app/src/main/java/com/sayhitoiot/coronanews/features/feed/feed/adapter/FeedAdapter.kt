package com.sayhitoiot.coronanews.features.feed.feed.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter.FeedAdapterPresent
import com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter.contract.FeedAdapterPresenterToView
import com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter.contract.FeedAdapterViewToPresenter
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val context: Context, var feedList: MutableList<FeedEntity>):
    RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    companion object{
        const val TAG = "feed-adapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_feed))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(feedList, position)
    }

    override fun getItemCount() = feedList.size

    fun updateList(feeds: MutableList<FeedEntity>) {
        this.feedList.clear()
        this.feedList.addAll(feeds)
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
        override var countryFavorite: Boolean
            get() = feedList[adapterPosition].favorite
            set(value) {}

        fun bind(feed: MutableList<FeedEntity>, position: Int){

            textCountry.text = feed[position].country
            textNewCases.text = feed[position].newCases
            textRecoveries.text = feed[position].recovereds.toString()
            textTotal.text = feed[position].cases.toString()
            textDeaths.text = feed[position].deaths.toString()
            if(feed[position].favorite) {
                favoriteButton.imageTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorAccent))
            } else {
                favoriteButton.imageTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorFineGray))
            }
            presenter.requestUpdateGraph()
            favoriteButton.setOnClickListener { presenter.favoriteButtonTapped() }

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

        override fun updateAdapter(feedUpdated: MutableList<FeedEntity>) {
            feedList.clear()
            feedList.addAll(feedUpdated)
            notifyDataSetChanged()
        }

        override fun showMessage(message: String) {
//            Toast.makeText(context, message, Toast.LENGTH_SHORT)
//                .show()
        }
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}