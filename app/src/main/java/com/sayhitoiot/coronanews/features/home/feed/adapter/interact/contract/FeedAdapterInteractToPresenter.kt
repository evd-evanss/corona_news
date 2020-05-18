package com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract

interface FeedAdapterInteractToPresenter {
    fun requestFavoriteFeedByCountryOnDB(country: String, handleFavorite: Boolean)
}