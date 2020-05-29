package com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter

import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.feed.adapter.interact.FeedAdapterInteract
import com.sayhitoiot.coronanews.features.feed.feed.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.feed.feed.adapter.interact.contract.FeedAdapterPresenterToInteract
import com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter.contract.FeedAdapterPresenterToView
import com.sayhitoiot.coronanews.features.feed.feed.adapter.presenter.contract.FeedAdapterViewToPresenter

class FeedAdapterPresent(private val view: FeedAdapterViewToPresenter)
    : FeedAdapterPresenterToView, FeedAdapterPresenterToInteract{

    companion object {
        const val TAG = "adapter-presenter"
    }

    private val interact: FeedAdapterInteractToPresenter by lazy {
        FeedAdapterInteract(this)
    }

    override fun requestUpdateGraph() {
        val recoveries = view.totalRecovered?.toFloat() ?: 0f
        val total = view.totalConfirmed?.toFloat() ?: 0f
        val deaths = view.totalDeaths?.toFloat() ?: 0f
        view.updateGraph(
            recoveries,
            total,
            deaths
        )
    }

    override fun favoriteButtonTapped() {

        interact.requestFavoriteItem(view.country, handleFavoriteCountry())
    }

    override fun didFetchDataForFeed(feedList: MutableList<FeedEntity>) {
        view.updateAdapter(feedList)
    }

    override fun requestMessageToast(message: String) {
        view.showMessage(message)
    }

    private fun handleFavoriteCountry() : Boolean {
        return !view.countryFavorite
    }

    private fun getColor(handleFavorite: Boolean) : Int {

        return when(handleFavorite) {
            true -> R.color.colorYellow
            else -> R.color.colorFineGray
        }

    }
}