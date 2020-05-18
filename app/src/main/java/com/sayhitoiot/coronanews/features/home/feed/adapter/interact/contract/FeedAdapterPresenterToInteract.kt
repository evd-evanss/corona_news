package com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedAdapterPresenterToInteract {
    fun didFavoriteFeedByCountry(handleFavorite: Boolean)
    fun didFetchFavorites(feedUpdated: MutableList<FeedEntity>)
}