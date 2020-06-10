package com.sayhitoiot.coronanews.features.feed.feed.view.adapter.presenter.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedAdapterViewToPresenter {
    fun updateAdapter(feedUpdated: MutableList<FeedEntity>, animation: Boolean)
    fun showMessage(fail: String)

    var totalRecovered : Int?
    var totalConfirmed : Int?
    var totalDeaths : Int?
    var country: String?
    var countryFavorite: Boolean
}