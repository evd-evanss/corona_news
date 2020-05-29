package com.sayhitoiot.coronanews.features.feed.feed.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.feed.interact.FeedInteract
import com.sayhitoiot.coronanews.features.feed.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.feed.feed.interact.contract.FeedPresenterToInteract
import com.sayhitoiot.coronanews.features.feed.feed.presenter.contract.FeedPresenterToView
import com.sayhitoiot.coronanews.features.feed.feed.presenter.contract.FeedViewToPresenter
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

    override fun filterData(text: String) {
        interact.fetDataByFilter(text)
    }

    override fun buttonSearchTapped() {
        view.renderViewForSearch()
    }

    override fun buttonBackTapped() {
        view.renderViewDefault()
    }

    override fun didFetchDataForFeed(feed: MutableList<FeedEntity>) {
        view.postValueInAdapter(feed)
    }

    override fun didFetchDataByFilter(feedFilter: MutableList<FeedEntity>) {
        view.postValueInAdapter(feedFilter)
    }

    override fun didFetchDataFail(fail: String) {
        view.renderViewWithFail(fail)
    }
}