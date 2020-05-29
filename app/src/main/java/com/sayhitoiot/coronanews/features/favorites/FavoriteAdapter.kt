package com.sayhitoiot.coronanews.features.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(private val context: Context, var feedList: MutableList<FeedEntity>):
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>(){

    companion object{
        const val TAG = "feed-adapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_favorite))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(feedList, position)
    }

    override fun getItemCount() = feedList.size

    fun updateList(properties: MutableList<FeedEntity>) {
        this.feedList.clear()
        this.feedList.addAll(properties)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private var textCountry: TextView = itemView.itemFavorite_textView_Country
        private var textRecoveries: TextView = itemView.itemFavorite_textView_recovered
        private var textTotal: TextView = itemView.itemFavorite_textView_Total
        private var textDeaths: TextView = itemView.itemFavorite_textView_Deaths
        private var statisticsButton: View = itemView.itemFavorite_materialButton_statistics

        fun bind(feed: MutableList<FeedEntity>, position: Int){

            textCountry.text = feed[position].country
            textRecoveries.text = feed[position].recovereds.toString()
            textTotal.text = feed[position].cases.toString()
            textDeaths.text = feed[position].deaths.toString()
            statisticsButton.setOnClickListener {  }

        }


    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}