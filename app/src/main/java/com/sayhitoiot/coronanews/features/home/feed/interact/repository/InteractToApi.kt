package com.sayhitoiot.coronanews.features.home.feed.interact.repository

import com.sayhitoiot.coronanews.api.OnGetStatisticsCoronaCallback

interface InteractToApi {

    fun getStatistics(callbackStatistics: OnGetStatisticsCoronaCallback)

}