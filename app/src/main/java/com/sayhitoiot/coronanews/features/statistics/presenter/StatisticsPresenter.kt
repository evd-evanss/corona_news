package com.sayhitoiot.coronanews.features.statistics.presenter

import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.statistics.interact.StatisticInteractToInteract
import com.sayhitoiot.coronanews.features.statistics.interact.StatisticPresenterToInteract
import com.sayhitoiot.coronanews.features.statistics.interact.StatisticsInteract
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToPresenter
import com.sayhitoiot.coronanews.features.statistics.presenter.contract.StatisticPresenterToView

class StatisticsPresenter(private val view: StatisticPresenterToView) : StatisticPresenterToPresenter,
    StatisticPresenterToInteract{

    private val interact: StatisticInteractToInteract by lazy {
        StatisticsInteract(this)
    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun didFinishInitializeViews() {
        interact.fetchDataOnDB(view.country)
    }

    override fun imageBackTapped() {
        view.renderPreviousView()
    }

    override fun didFinishFetchDataOnDB(feed: MutableList<FeedEntity>) {
        val feedEntity = feed[0]
        view.showStatistics(feedEntity, getStatistics(feedEntity))

    }

    private fun getStatistics(feedEntity: FeedEntity): MutableList<Float> {
        val rateRecoveries = (feedEntity.recovereds.toFloat() / feedEntity.cases.toFloat()) * 100
        val rateMortality = (feedEntity.deaths.toFloat() / feedEntity.cases.toFloat()) * 100

        val rates: MutableList<Float> = mutableListOf()
        rates.add(rateRecoveries)
        rates.add(rateMortality)
        "%.2f".format(rateMortality)

        view.updateGraph(
            feedEntity.recovereds.toFloat()/2500,
            feedEntity.cases.toFloat()/4000,
            feedEntity.deaths.toFloat()/1000,
            rateRecoveries,
            rateMortality)

        return rates

    }
}