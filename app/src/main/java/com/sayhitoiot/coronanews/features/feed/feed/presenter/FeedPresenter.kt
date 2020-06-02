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

    companion object {
        const val MORE = 0
        const val FEWER = 1
        const val CONTINENTS = 2
        const val ALL = 3
    }

    private var handleMenu = false

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

    override fun moreCasesTapped() {
        interact.setFilter(MORE)
    }

    override fun fewerCasesTapped() {
        interact.setFilter(FEWER)
    }

    override fun continentCasesTapped() {
        interact.setFilter(CONTINENTS)
    }

    override fun allCasesTapped() {
        interact.setFilter(ALL)
    }

    override fun imageMenuTapped() {
        handleMenu= !handleMenu
        when(handleMenu) {
            true -> view.openMenuFilters()
            false -> view.closeMenuFilters()
        }
    }

    override fun buttonSearchTapped() {
        view.renderViewForSearch()
    }

    override fun buttonBackTapped() {
        view.renderViewDefault()
    }

    override fun didFetchDataForFeed(
        feed: MutableList<FeedEntity>,
        filter: Int
    ) {
        view.postValueInAdapter(feed)
        setFilter(filter)
    }

    private fun setFilter(filter: Int) {
        when(filter) {
            0 -> view.selectMoreFilterButton()
            1 -> view.selectFewerFilterButton()
            2 -> view.selectContinentFilterButton()
            3 -> view.selectAllFilterButton()
        }

    }

    override fun didFetchDataByFilter(
        feedFilter: MutableList<FeedEntity>,
        filter: Int
    ) {
        view.postValueInAdapter(feedFilter)
        setFilter(filter)
    }

    override fun didFetchDataFail(fail: String) {
        view.renderViewWithFail(fail)
    }
}