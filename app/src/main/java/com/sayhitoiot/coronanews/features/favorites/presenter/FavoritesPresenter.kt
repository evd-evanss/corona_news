package com.sayhitoiot.coronanews.features.favorites.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.favorites.interact.FavoritesInteract
import com.sayhitoiot.coronanews.features.favorites.interact.contract.FavoriteInteractToInteract
import com.sayhitoiot.coronanews.features.favorites.interact.contract.FavoriteInteractToPresenter
import com.sayhitoiot.coronanews.features.favorites.presenter.contract.FavoritesToPresenter
import com.sayhitoiot.coronanews.features.favorites.presenter.contract.FavoritesToView

class FavoritesPresenter(private val view: FavoritesToView) : FavoritesToPresenter,
    FavoriteInteractToInteract{

    private val interact: FavoriteInteractToPresenter by lazy {
        FavoritesInteract(this)
    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun onResume() {
        interact.fetchFeedOnDB()
    }

    override fun didFinishInitializeViews() {
        interact.fetchFeedOnDB()
    }

    override fun didFetchFeed(feedFavorites: MutableList<FeedEntity>) {
        view.postFeedInAdapter(feedFavorites)
    }
}