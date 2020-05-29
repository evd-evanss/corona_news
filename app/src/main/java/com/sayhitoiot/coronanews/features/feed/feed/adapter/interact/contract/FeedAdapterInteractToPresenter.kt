package com.sayhitoiot.coronanews.features.feed.feed.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteItem(country: String?, favorite: Boolean)
}