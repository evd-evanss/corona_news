package com.sayhitoiot.coronanews.features.feed.feed.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedPresenterToInteract {
    fun didFetchDataForFeed(
        feed: MutableList<FeedEntity>,
        filter: Int,
        animation: Boolean
    )
    fun didFetchDataByFilter(
        feedFilter: MutableList<FeedEntity>,
        filter: Int
    )
    fun didFetchDataFail(fail: String)
}