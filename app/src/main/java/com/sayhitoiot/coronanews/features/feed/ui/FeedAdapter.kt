package com.sayhitoiot.coronanews.features.feed.ui

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter(private val context: Context, var feedList: MutableList<FeedEntity>):
    RecyclerView.Adapter<FeedAdapter.ViewHolder>(){

    companion object{
        const val TAG = "feed-adapter"
        const val H = 200f
        const val ALPHA = 0.3f
        const val DURATION = 1000L
    }

    lateinit var viewModel: FeedViewModel
    private var animation = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_feed))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(feedList, position)
    }

    override fun getItemCount() = feedList.size
    private var statusAnimation = false

    fun updateList(
        feeds: MutableList<FeedEntity>,
        animation: Boolean,
        viewModel: FeedViewModel
    ) {
        this.statusAnimation = animation
        this.feedList.clear()
        this.feedList.addAll(feeds)
        this.viewModel = viewModel
        setupObserverAnimation()
        notifyDataSetChanged()
    }

    private fun setupObserverAnimation() {
        viewModel.animation.observe(context as LifecycleOwner, androidx.lifecycle.Observer {
            animation = it
        })
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private var textCountry: TextView = itemView.itemFeed_textView_Country
        private var textCount: TextView = itemView.itemFeed_textView_count
        private var textNewCases: TextView = itemView.itemFeed_textView_newCases
        private var textRecoveries: TextView = itemView.itemFeed_textView_recovered
        private var textTotal: TextView = itemView.itemFeed_textView_Total
        private var textDeaths: TextView = itemView.itemFeed_textView_Deaths
        private var favoriteButton: ImageView = itemView.itemFeed_imageView_favorite
        private val enterInterpolator = AnticipateOvershootInterpolator(3f)

        fun bind(feed: MutableList<FeedEntity>, position: Int){
            val count ="${adapterPosition+1}º"
            textCount.text = count
            textCountry.text = feed[position].country
            textNewCases.text = feed[position].newCases
            textRecoveries.text = feed[position].recovereds.toString()
            textTotal.text = feed[position].cases.toString()
            textDeaths.text = feed[position].deaths.toString()
            if(feed[position].favorite) {
                favoriteButton.imageTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorYellow))
            } else {
                favoriteButton.imageTintList = ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorFineGray))
            }
            favoriteButton.setOnClickListener { viewModel.favoriteFeed(feed[position].country, feed[position].favorite) }
            setAnimation(itemView)
        }

        private fun setAnimation(child: View) {
            if(animation) {
                child.translationY = if(adapterPosition == 0) -H else H
                child.alpha =
                    ALPHA
                child.animate().translationY(0f).alpha(1f)
                    .setInterpolator(enterInterpolator).duration = DURATION
            }
        }
    }

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}