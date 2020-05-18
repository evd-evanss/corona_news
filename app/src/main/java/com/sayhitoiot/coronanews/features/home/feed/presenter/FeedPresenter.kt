package com.sayhitoiot.coronanews.features.home.feed.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.interact.FeedInteract
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedPresenterToInteract
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.home.feed.presenter.contract.FeedViewToPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class FeedPresenter(private val view: FeedViewToPresenter)
    : FeedPresenterToView, FeedPresenterToInteract, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private val interact: FeedInteractToPresenter by lazy {
        FeedInteract(this)
    }

    override fun onViewCreated() {
        view.initializeViews()
    }

    override fun onResume() {
        interact.fetchDataForFeed()
    }

    override fun didFinishInitializeViews() {
        interact.fetchDataForFeed()
    }

    override fun buttonSearchTapped() {
        view.renderViewForSearch()
    }

    override fun buttonBackTapped() {
        view.renderViewDefault()
    }

    override fun didFetchDataForFeed(feed: MutableList<FeedEntity>) {
        if(feed.size == 0) {
            interact.fetchDataInApiCovid()
        }
        view.postValueInAdapter(feed)
    }
}