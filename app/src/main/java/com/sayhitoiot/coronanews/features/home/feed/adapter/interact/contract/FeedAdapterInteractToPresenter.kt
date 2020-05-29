package com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteItem(country: String?, favorite: Boolean)
}