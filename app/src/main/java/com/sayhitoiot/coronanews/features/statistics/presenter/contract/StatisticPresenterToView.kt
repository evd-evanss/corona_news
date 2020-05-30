package com.sayhitoiot.coronanews.features.statistics.presenter.contract

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

interface StatisticPresenterToView {
    var country: String?
    fun showStatistics(
        feed: FeedEntity,
        statistics: MutableList<Float>
    )
    fun initializeViews()

    fun updateGraph(
        recoveries: Float,
        total: Float,
        deaths: Float,
        rateRecovery: Float,
        rateDeaths: Float
    )

    fun renderPreviousView()
}