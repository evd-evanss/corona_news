package com.sayhitoiot.coronanews.features.home.feed.adapter.presenter

import android.util.Log
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.FeedAdapterInteract
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterPresenterToInteract
import com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract.FeedAdapterPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.adapter.presenter.contract.FeedAdapterViewToPresenter

class FeedAdapterPresent(private val view: FeedAdapterViewToPresenter)
    : FeedAdapterPresenterToView, FeedAdapterPresenterToInteract{

    companion object {
        const val TAG = "adapter-presenter"
    }

    private val interact: FeedAdapterInteractToPresenter by lazy {
        FeedAdapterInteract(this)
    }

    private var handleFavorite: Boolean =  false

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


    override fun buttonFavoriteTapped() {
        handleFavorite = !handleFavorite

        if(handleFavorite) {
            view.country?.let { interact.requestFavoriteFeedByCountry(it, handleFavorite) }
        } else {
            view.country?.let { interact.requestFavoriteFeedByCountry(it, handleFavorite) }
        }
    }

    override fun didFavoriteFeedByCountry(handleFavorite: Boolean) {

        view.renderViewFavorite(getColor(handleFavorite))

    }

    override fun didFetchFavorites(feedUpdated: MutableList<FeedEntity>) {
        view.updateAdapterWithFavorites(feedUpdated)
    }

    private fun getColor(handleFavorite: Boolean) : Int {

        return when(handleFavorite) {
            true -> R.color.colorYellow
            else -> R.color.colorFineGray
        }

    }
}