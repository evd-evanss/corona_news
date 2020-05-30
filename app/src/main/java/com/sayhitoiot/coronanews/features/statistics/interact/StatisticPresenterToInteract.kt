package com.sayhitoiot.coronanews.features.statistics.interact

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface StatisticPresenterToInteract {
    fun didFinishFetchDataOnDB(feed: MutableList<FeedEntity>)
}