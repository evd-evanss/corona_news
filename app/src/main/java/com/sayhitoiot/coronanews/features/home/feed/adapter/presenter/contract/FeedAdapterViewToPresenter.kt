package com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedAdapterViewToPresenter {
    fun updateGraph(recoveries: Float, total: Float, deaths: Float)
    fun renderViewFavorite(color: Int)
    fun updateAdapterWithFavorites(feedUpdated: MutableList<FeedEntity>)

    var totalRecovered : Int?
    var totalConfirmed : Int?
    var totalDeaths : Int?
    var country: String?
}