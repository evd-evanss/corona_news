package com.sayhitoiot.coronanews.features.feed.feed.presenter.contract

import android.app.Activity
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedViewToPresenter {
    var activity: Activity?
    fun postValueInAdapter(
        feed: MutableList<FeedEntity>,
        animation: Boolean
    )
    fun initializeViews()
    fun renderViewForSearch()
    fun renderViewDefault()
    fun renderViewWithFail(fail: String)
    fun selectMoreFilterButton()
    fun selectFewerFilterButton()
    fun selectAllFilterButton()
    fun selectContinentFilterButton()
    fun openMenuFilters()
    fun closeMenuFilters()
}