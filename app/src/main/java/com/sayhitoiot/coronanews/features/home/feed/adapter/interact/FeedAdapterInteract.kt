package com.sayhitoiot.coronanews.features.home.feed.adapter.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterPresenterToInteract

class FeedAdapterInteract (private val presenter: FeedAdapterPresenterToInteract)
    : FeedAdapterInteractToPresenter {

    companion object {
        const val TAG = "interact-adapter"
        const val FEEDS = "Feeds"
        const val FAVORITE = "favorite"
        const val INCLUSION_MESSAGE = "incluído nos favoritos"
        const val REMOVAL_MESSAGE = "removido dos favoritos"
        const val FAIL = "falha ao favoritar"
    }

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun requestFavoriteItem(country: String?, favorite: Boolean) {
        if (country == null) {
            presenter.requestMessageToast(FAIL)
        } else {
            Log.d(TAG, "$favorite")
            FeedEntity.update(country, favorite)
            favoriteFeedInFirebase(country, favorite)
            fetchFeedUpdated()
            when {
                favorite -> presenter.requestMessageToast("$country $INCLUSION_MESSAGE")
                !favorite -> presenter.requestMessageToast("$country $REMOVAL_MESSAGE")
            }
        }

    }

    private fun favoriteFeedInFirebase(country: String, favorite: Boolean) {
        val uid = mAuth.uid!!
        firebaseDatabase.reference
            .child(uid)
            .child(FEEDS)
            .child(country)
            .child(FAVORITE)
            .setValue(favorite)
    }

    private fun fetchFeedUpdated() {
        val feedUpdated = FeedEntity.getAll()
        presenter.didFetchDataForFeed(feedUpdated)
    }
}