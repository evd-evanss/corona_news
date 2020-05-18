package com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteFeedByCountry(country: String, handleFavorite: Boolean)
}