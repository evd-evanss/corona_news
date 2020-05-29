package com.sayhitoiot.coronanews.features.favorites.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FavoriteInteractToInteract {
    fun didFetchFeed(feedFavorites: MutableList<FeedEntity>)
}