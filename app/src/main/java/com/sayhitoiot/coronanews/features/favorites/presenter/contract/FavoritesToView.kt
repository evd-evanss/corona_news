package com.sayhitoiot.coronanews.features.favorites.presenter.contract

import android.app.Activity
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FavoritesToView {
    val activity: Activity?
    fun postFeedInAdapter(feedFavorites: MutableList<FeedEntity>)
    fun initializeViews()
}