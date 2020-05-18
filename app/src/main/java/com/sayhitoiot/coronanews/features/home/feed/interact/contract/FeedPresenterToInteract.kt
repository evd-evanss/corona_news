package com.sayhitoiot.coronanews.features.home.feed.interact.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface FeedPresenterToInteract {
    fun didFetchDataForFeed(feed: MutableList<FeedEntity>)
}