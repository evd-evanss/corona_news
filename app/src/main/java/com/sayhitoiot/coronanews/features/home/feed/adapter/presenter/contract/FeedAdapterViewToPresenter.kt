package com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedAdapterViewToPresenter {
    fun updateGraph(recoveries: Float, total: Float, deaths: Float)
    fun updateAdapterWithFavorites(feedUpdated: MutableList<FeedEntity>)
    fun updateAdapter(feedUpdated: MutableList<FeedEntity>)
    fun showMessage(message: String)

    var totalRecovered : Int?
    var totalConfirmed : Int?
    var totalDeaths : Int?
    var country: String?
    var countryFavorite: Boolean
}