package com.sayhitoiot.coronanews.features.statistics.interact

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

class StatisticsInteract(private val presenter: StatisticPresenterToInteract) : StatisticInteractToInteract{

    override fun fetchDataOnDB(country: String?) {
        if(country != null) {
            val feed = FeedEntity.findByFilter(country)
            presenter.didFinishFetchDataOnDB(feed)
        }
    }
}