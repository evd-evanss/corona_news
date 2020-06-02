package com.sayhitoiot.coronanews.features.feed.feed.interact.contract

interface FeedInteractToPresenter {
    fun fetchDataForFeed()
    fun fetDataByFilter(text: String)
    fun setFilter(filter: Int)
}