package com.sayhitoiot.coronanews.features.feed.view.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteItem(country: String?, favorite: Boolean)
}