package com.sayhitoiot.coronanews.features.home.feed.adapter.interact

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterPresenterToInteract

class FeedAdapterInteract (private val presenter: FeedAdapterPresenterToInteract)
    : FeedAdapterInteractToPresenter {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun requestFavoriteFeedByCountry(country: String, handleFavorite: Boolean) {
        FeedEntity.favoriteFeed(country, handleFavorite)
        presenter.didFavoriteFeedByCountry(handleFavorite)
        updateFeedOnFirebase()
        FeedEntity.favoriteFeed(country, handleFavorite)
    }

    private fun updateFeedOnFirebase() {
        val feedList = FeedEntity.getAll()
        val uid = mAuth.uid ?: return
        val userReference =
            firebaseDatabase.reference.child(uid).child("Feeds")
        userReference.setValue(feedList)
    }


}