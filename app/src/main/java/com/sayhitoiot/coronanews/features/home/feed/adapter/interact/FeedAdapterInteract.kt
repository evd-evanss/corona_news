package com.sayhitoiot.coronanews.features.home.feed.adapter.interact

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.adapter.interact.contract.FeedAdapterPresenterToInteract

class FeedAdapterInteract (private val presenter: FeedAdapterPresenterToInteract)
    : FeedAdapterInteractToPresenter {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()

}