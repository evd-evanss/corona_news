package com.sayhitoiot.coronanews.commom.apicovid

import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.model_states.ResultOfStates
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiCovid{

    @GET("statistics")
    fun getStatistics(
        @Header("x-rapidapi-host") host: String,
        @Header("x-rapidapi-key") key: String
    ): Call<ResultData>

    @GET("report/v1")
    fun getStatesOfBrazil(): Call<ResultOfStates>

}

interface OnGetStatisticsCoronaCallback{
    fun onSuccess(coronaResult: ResultData)
    fun onError()
}

interface OnGetStatisticsByStatesCoronaCallback{
    fun onSuccess(coronaResult: ResultOfStates)
    fun onError()
}
