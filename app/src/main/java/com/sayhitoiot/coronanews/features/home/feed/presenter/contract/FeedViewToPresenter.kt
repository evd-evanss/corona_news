package com.sayhitoiot.coronanews.features.home.feed.presenter.contract

import android.app.Activity
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedViewToPresenter {
    var activity: Activity?
    fun postValueInAdapter(feed: MutableList<FeedEntity>)
    fun initializeViews()
    fun renderViewForSearch()
    fun renderViewDefault()
    fun renderViewWithFail(fail: String)
}