package com.sayhitoiot.coronanews.features.home.feed.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.interact.FeedInteract
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedPresenterToInteract
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedViewToPresenter

class FeedPresenter(private val view: FeedViewToPresenter)
    : FeedPresenterToView, FeedPresenterToInteract {

    private val interact: FeedInteractToPresenter by lazy {
        FeedInteract(this)
    }

    override fun onViewCreated() {
        interact.fetchDataForFeed()
        view.initializeViews()
    }

    override fun didFetchDataForFeed(feed: MutableList<FeedEntity>) {
        view.postValueInAdapter(feed)
    }
}