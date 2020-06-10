package com.sayhitoiot.coronanews.features.feed.feed.view.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteItem(country: String?, favorite: Boolean)
}