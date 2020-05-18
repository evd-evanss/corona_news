package com.sayhitoiot.coronanews.commom.apicovid.repository

import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback

interface InteractToApi {

    fun getStatistics(callbackStatistics: OnGetStatisticsCoronaCallback)

}