package com.sayhitoiot.coronanews.features.feed.view.adapter.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedAdapterPresenterToInteract {
    fun didFetchDataForFeed(feedList: MutableList<FeedEntity>)
    fun requestMessageToast(fail: String)
}