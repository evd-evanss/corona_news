package com.sayhitoiot.coronanews.features.favorites

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.statistics.StatisticActivity
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

        private var statisticsButton: MaterialButton = itemView.itemFavorite_materialButton_statistics

        @SuppressLint("ResourceAsColor")
        fun bind(feed: MutableList<FeedEntity>, position: Int){

            statisticsButton.text = feed[position].country

            statisticsButton.setOnClickListener { startStatisticActivity(feed[position].country) }

        }

        private fun startStatisticActivity(country: String) {
            val intent = Intent(context, StatisticActivity::class.java)
            intent.putExtra("country", country)
            context.startActivity(intent)
        }


    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}