package com.sayhitoiot.coronanews.features.favorites.interact

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.favorites.interact.contract.FavoriteInteractToInteract
import com.sayhitoiot.coronanews.features.favorites.interact.contract.FavoriteInteractToPresenter

class FavoritesInteract(private val presenter: FavoriteInteractToInteract) : FavoriteInteractToPresenter {


    override fun fetchFeedOnDB() {
        getAllFavorites()
    }

    private fun getAllFavorites() {
        val feedFavorites = FeedEntity.getAllFavorites()
        if(feedFavorites.isNotEmpty()) {
            presenter.didFetchFeed(feedFavorites)
        }
    }
}