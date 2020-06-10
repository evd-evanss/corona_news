package com.sayhitoiot.coronanews.features.feed.feed.view.adapter.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.feed.view.adapter.interact.FeedAdapterInteract
import com.sayhitoiot.coronanews.features.feed.feed.view.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.feed.feed.view.adapter.interact.contract.FeedAdapterPresenterToInteract
import com.sayhitoiot.coronanews.features.feed.feed.view.adapter.presenter.contract.FeedAdapterPresenterToView
import com.sayhitoiot.coronanews.features.feed.feed.view.adapter.presenter.contract.FeedAdapterViewToPresenter

class FeedAdapterPresent(private val view: FeedAdapterViewToPresenter)
    : FeedAdapterPresenterToView, FeedAdapterPresenterToInteract{

    companion object {
        const val TAG = "adapter-presenter"
    }

    private val interact: FeedAdapterInteractToPresenter by lazy {
        FeedAdapterInteract(this)
    }

    override fun requestUpdateGraph() {
        view.totalRecovered?.toFloat()
        view.totalConfirmed?.toFloat()
        view.totalDeaths?.toFloat()
    }

    override fun favoriteButtonTapped() {
        interact.requestFavoriteItem(view.country, handleFavoriteCountry())
    }

    override fun didFetchDataForFeed(feedList: MutableList<FeedEntity>) {
        view.updateAdapter(feedList, false)
    }

    private fun handleFavoriteCountry() : Boolean {
        return !view.countryFavorite
    }

    override fun requestMessageToast(fail: String) {
        view.showMessage(fail)
    }
}